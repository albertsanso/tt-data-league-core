package org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.model.PlayersSingleMatchJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerResultToSeasonPlayerResultJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PlayersSingleMatchToPlayersSingleMatchJPAMapper implements Function<PlayersSingleMatch, PlayersSingleMatchJPA> {

    private final SeasonPlayerResultToSeasonPlayerResultJPAMapper toJpaMapper;

    @Autowired
    public PlayersSingleMatchToPlayersSingleMatchJPAMapper(SeasonPlayerResultToSeasonPlayerResultJPAMapper toJpaMapper) {
        this.toJpaMapper = toJpaMapper;
    }

    @Override
    public PlayersSingleMatchJPA apply(PlayersSingleMatch playersSingleMatch) {
        PlayersSingleMatchJPA playersSingleMatchJPA = new PlayersSingleMatchJPA();
        playersSingleMatchJPA.setId(playersSingleMatch.getId());
        playersSingleMatchJPA.setSeasonPlayerResultAbc(toJpaMapper.apply(playersSingleMatch.getSeasonPlayerResultAbc()));
        playersSingleMatchJPA.setSeasonPlayerResultXyz(toJpaMapper.apply(playersSingleMatch.getSeasonPlayerResultXyz()));
        playersSingleMatchJPA.setSeason(playersSingleMatch.getSeason());
        playersSingleMatchJPA.setCompetitionType(playersSingleMatch.getCompetitionType());
        playersSingleMatchJPA.setCompetitionCategory(playersSingleMatch.getCompetitionCategory());
        playersSingleMatchJPA.setCompetitionScope(playersSingleMatch.getCompetitionScope());
        playersSingleMatchJPA.setCompetitionScopeTag(playersSingleMatch.getCompetitionScopeTag());
        playersSingleMatchJPA.setCompetitionGroup(playersSingleMatch.getCompetitionGroup());
        playersSingleMatchJPA.setCompetitionGender(playersSingleMatch.getCompetitionGender());
        playersSingleMatchJPA.setMatchDayNumber(playersSingleMatch.getMatchDayNumber());
        playersSingleMatchJPA.setUniqueRowMatchId(playersSingleMatch.getUniqueRowMatchId());
        return playersSingleMatchJPA;
    }
}
