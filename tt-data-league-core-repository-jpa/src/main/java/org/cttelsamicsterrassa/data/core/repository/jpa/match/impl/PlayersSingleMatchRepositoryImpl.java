package org.cttelsamicsterrassa.data.core.repository.jpa.match.impl;

import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.domain.repository.PlayersSingleMatchRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchJPAToPlayersSingleMatchMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchToPlayersSingleMatchJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return helper.findBySeasonAndCompetitionTypeAndCompetitionCategoryAndCompetitionScopeAndCompetitionScopeTagAndCompetitionGroupAndMatchDayNumber(
                season,
                competitionInfo.competitionType(),
                competitionInfo.competitionCategory(),
                competitionInfo.competitionScope(),
                competitionInfo.competitionScopeTag(),
                competitionInfo.competitionGroup(),
                matchDayNumber
        ).stream().map(fromJpaMapper).toList();
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
