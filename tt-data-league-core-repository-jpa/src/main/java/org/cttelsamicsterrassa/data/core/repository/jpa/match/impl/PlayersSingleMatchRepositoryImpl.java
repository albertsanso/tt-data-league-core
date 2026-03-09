package org.cttelsamicsterrassa.data.core.repository.jpa.match.impl;

import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.domain.repository.PlayersSingleMatchRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchJPAToPlayersSingleMatchMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchToPlayersSingleMatchJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.model.PlayersSingleMatchJPA;
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
    public Optional<PlayersSingleMatch> findBySeasonPlayerResultAbcIdAndSeasonPlayerResultXyzIdAndUniqueId(UUID seasonPlayerResultAbcId, UUID seasonPlayerResultXyzId, String uniqueId) {
        return helper.findBySeasonPlayerResultAbc_IdAndSeasonPlayerResultXyz_IdAndUniqueRowMatchId(
                seasonPlayerResultAbcId, seasonPlayerResultXyzId, uniqueId)
                    .map(fromJpaMapper);
    }

    @Override
    public List<PlayersSingleMatch> findBySeasonAndCompetitionAndMatchDayNumber(String season, CompetitionInfo competitionInfo, int matchDayNumber) {
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
        return helper.findAll(spec).stream().map(fromJpaMapper).toList();
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
