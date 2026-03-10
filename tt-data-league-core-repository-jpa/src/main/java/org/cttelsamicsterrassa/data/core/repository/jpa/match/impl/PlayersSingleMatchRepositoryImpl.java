package org.cttelsamicsterrassa.data.core.repository.jpa.match.impl;

import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayerResult;
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
    public Optional<PlayersSingleMatch> findBySeasonPlayerResultLocalIdAndSeasonPlayerResultVisitorIdAndUniqueId(UUID seasonPlayerResultLocalId, UUID seasonPlayerResultVisitorId, String uniqueId) {
        return helper.findBySeasonPlayerResultLocal_IdAndSeasonPlayerResultVisitor_IdAndUniqueRowMatchId(
                        seasonPlayerResultLocalId, seasonPlayerResultVisitorId, uniqueId)
                    .map(fromJpaMapper);
    }

    @Override
    public List<PlayersSingleMatch> findBySeasonAndCompetitionAndMatchDayNumber(String season, CompetitionInfo competitionInfo, int matchDayNumber, String practitionerName) {

        Specification<PlayersSingleMatchJPA> spec = new SpecificationBuilder<PlayersSingleMatchJPA>()
                .equalIfPresent("season", season)
                .equalIfPresent("competitionType", competitionInfo.competitionType())
                .equalIfPresent("competitionCategory", competitionInfo.competitionCategory())
                .equalIfPresent("competitionScope", competitionInfo.competitionScope())
                .equalIfPresent("competitionScopeTag", competitionInfo.competitionScopeTag())
                .equalIfPresent("competitionGroup", competitionInfo.competitionGroup())
                .equalIfPresent("competitionGender", competitionInfo.competitionGender())
                .equalIfPresent("matchDayNumber", matchDayNumber)
                .build();

        return helper.findAll(practitionerName == null || practitionerName.isEmpty() ?
                        spec : spec.or(buildPracticionerNameSpec(practitionerName)))
                .stream()
                .map(fromJpaMapper)
                .toList();
    }

    private Specification<PlayersSingleMatchJPA> buildPracticionerNameSpec(String practitionerName) {
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
                    cb.like(localPracticionerJoin.get("fullName"), "%" + practitionerName + "%"),
                    cb.like(visitorPracticionerJoin.get("fullName"), "%" + practitionerName + "%")
            );
        };
    }

    @Override
    public List<PlayersSingleMatch> findAll() {
        return helper.findAll().stream().map(fromJpaMapper).toList();
    }

    @Override
    public void save(PlayersSingleMatch playerSingleMatch) {
        helper.save(toJpaMapper.apply(playerSingleMatch));
    }
}
