package org.cttelsamicsterrassa.data.core.repository.jpa.match.impl;

import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.domain.repository.PlayersSingleMatchRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.model.ClubMemberJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchJPAToPlayersSingleMatchMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchToPlayersSingleMatchJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.model.PlayersSingleMatchJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerResultJPA;
import org.cttelsamicsterrassa.data.core.repository.shared.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class PlayersSingleMatchRepositoryImpl implements PlayersSingleMatchRepository {

    private final PlayersSingleMatchRepositoryHelper helper;

    private final PlayersSingleMatchJPAToPlayersSingleMatchMapper fromJpaMapper;

    private final PlayersSingleMatchToPlayersSingleMatchJPAMapper toJpaMapper;

    @Autowired
    public PlayersSingleMatchRepositoryImpl(PlayersSingleMatchRepositoryHelper helper, PlayersSingleMatchJPAToPlayersSingleMatchMapper fromJpaMapper, PlayersSingleMatchToPlayersSingleMatchJPAMapper toJpaMapper) {
        this.helper = helper;
        this.fromJpaMapper = fromJpaMapper;
        this.toJpaMapper = toJpaMapper;
    }

    @Override
    public Optional<PlayersSingleMatch> findById(UUID id) {
        return helper.findById(id)
                .map(fromJpaMapper);
    }

    @Override
    public List<PlayersSingleMatch> findByTeamName(String teamName) {
        if (teamName == null || teamName.isEmpty()) {
            return List.of();
        }

        Specification<PlayersSingleMatchJPA> spec = buildTeamNameSpec(teamName);
        return helper.findAll(spec).stream().map(fromJpaMapper).toList();
    }

    @Override
    public Optional<PlayersSingleMatch> findBySeasonPlayerResultLocalIdAndSeasonPlayerResultVisitorIdAndUniqueId(UUID seasonPlayerResultLocalId, UUID seasonPlayerResultVisitorId, String uniqueId) {
        return helper.findBySeasonPlayerResultLocal_IdAndSeasonPlayerResultVisitor_IdAndUniqueRowMatchId(
                        seasonPlayerResultLocalId, seasonPlayerResultVisitorId, uniqueId)
                    .map(fromJpaMapper);
    }

    @Override
    public List<PlayersSingleMatch> findBySeasonAndCompetitionAndMatchDayNumber(String season, CompetitionInfo competitionInfo, Integer matchDayNumber, String practitionerName) {

        Specification<PlayersSingleMatchJPA> spec = new SpecificationBuilder<PlayersSingleMatchJPA>()
                .equalIfPresent("season", season)
                .equalIfPresent("competitionType", competitionInfo == null ? null : competitionInfo.competitionType())
                .equalIfPresent("competitionCategory", competitionInfo == null ? null : competitionInfo.competitionCategory())
                .equalIfPresent("competitionScope", competitionInfo == null ? null : competitionInfo.competitionScope())
                .equalIfPresent("competitionScopeTag", competitionInfo == null ? null : competitionInfo.competitionScopeTag())
                .equalIfPresent("competitionGroup", competitionInfo == null ? null : competitionInfo.competitionGroup())
                .equalIfPresent("competitionGender", competitionInfo == null ? null : competitionInfo.competitionGender())
                .equalIfPresent("matchDayNumber", matchDayNumber)
                .build();

        return helper.findAll(practitionerName == null || practitionerName.isBlank() ?
                        spec : spec.and(buildPracticionerNameSpec(practitionerName)))
                .stream()
                .map(fromJpaMapper)
                .toList();
    }

    private Specification<PlayersSingleMatchJPA> buildPracticionerNameSpec(String practitionerName) {
        String normalizedPractitionerName = "%" + practitionerName.toLowerCase() + "%";

        return (root, query, cb) -> {
            Join<PlayersSingleMatchJPA, SeasonPlayerResultJPA> localJoin = root.join("seasonPlayerResultLocal");
            Join<SeasonPlayerResultJPA, SeasonPlayerJPA> localSeasonPlayerJoin = localJoin.join("seasonPlayer");
            Join<SeasonPlayerJPA, ClubMemberJPA> localClubMemberJoin = localSeasonPlayerJoin.join("clubMember");
            Join<ClubMemberJPA, PracticionerJPA> localPracticionerJoin = localClubMemberJoin.join("practicioner");

            Join<PlayersSingleMatchJPA, SeasonPlayerResultJPA> visitorJoin = root.join("seasonPlayerResultVisitor");
            Join<SeasonPlayerResultJPA, SeasonPlayerJPA> visitorSeasonPlayerJoin = visitorJoin.join("seasonPlayer");
            Join<SeasonPlayerJPA, ClubMemberJPA> visitorClubMemberJoin = visitorSeasonPlayerJoin.join("clubMember");
            Join<ClubMemberJPA, PracticionerJPA> visitorPracticionerJoin = visitorClubMemberJoin.join("practicioner");

            return cb.or(
                    cb.like(cb.lower(localPracticionerJoin.get("fullName")), normalizedPractitionerName),
                    cb.like(cb.lower(visitorPracticionerJoin.get("fullName")), normalizedPractitionerName)
            );
        };
    }

    private Specification<PlayersSingleMatchJPA> buildTeamNameSpec(String teamName) {
        return (root, query, cb) -> {
            Join<PlayersSingleMatchJPA, SeasonPlayerResultJPA> localJoin = root.join("seasonPlayerResultLocal");
            Join<SeasonPlayerResultJPA, SeasonPlayerJPA> localSeasonPlayerJoin = localJoin.join("seasonPlayer");
            Join<SeasonPlayerJPA, ClubMemberJPA> localClubMemberJoin = localSeasonPlayerJoin.join("clubMember");

            Join<PlayersSingleMatchJPA, SeasonPlayerResultJPA> visitorJoin = root.join("seasonPlayerResultVisitor");
            Join<SeasonPlayerResultJPA, SeasonPlayerJPA> visitorSeasonPlayerJoin = visitorJoin.join("seasonPlayer");
            Join<SeasonPlayerJPA, ClubMemberJPA> visitorClubMemberJoin = visitorSeasonPlayerJoin.join("clubMember");

            return cb.or(
                    cb.like(localClubMemberJoin.get("club").get("name"), "%" + teamName + "%"),
                    cb.like(visitorClubMemberJoin.get("club").get("name"), "%" + teamName + "%")
            );
        };
    }

    @Override
    public List<PlayersSingleMatch> findAll() {
        return helper.findAll().stream().map(fromJpaMapper).toList();
    }

    @Override
    public void deteleById(UUID id) {
        helper.deleteById(id);
    }

    @Override
    public void save(PlayersSingleMatch playerSingleMatch) {
        helper.save(toJpaMapper.apply(playerSingleMatch));
    }
}
