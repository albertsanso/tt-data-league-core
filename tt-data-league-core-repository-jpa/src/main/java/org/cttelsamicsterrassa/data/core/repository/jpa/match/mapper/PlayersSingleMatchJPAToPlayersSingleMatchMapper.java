package org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.MatchInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.model.PlayersSingleMatchJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerResultJPAToSeasonPlayerResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PlayersSingleMatchJPAToPlayersSingleMatchMapper implements Function<PlayersSingleMatchJPA, PlayersSingleMatch> {

    private final SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper;

    @Autowired
    public PlayersSingleMatchJPAToPlayersSingleMatchMapper(SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper) {
        this.fromJPAMapper = fromJPAMapper;
    }

    @Override
    public PlayersSingleMatch apply(PlayersSingleMatchJPA playersSingleMatchJPA) {
        CompetitionInfo competitionInfo = new CompetitionInfo(
                playersSingleMatchJPA.getCompetitionType(),
                playersSingleMatchJPA.getCompetitionCategory(),
                playersSingleMatchJPA.getCompetitionScope(),
                playersSingleMatchJPA.getCompetitionScopeTag(),
                playersSingleMatchJPA.getCompetitionGroup(),
                playersSingleMatchJPA.getCompetitionGender()
        );

        return PlayersSingleMatch.createExisting(
                playersSingleMatchJPA.getId(),
                fromJPAMapper.apply(playersSingleMatchJPA.getSeasonPlayerResultAbc()),
                fromJPAMapper.apply(playersSingleMatchJPA.getSeasonPlayerResultXyz()),
                playersSingleMatchJPA.getSeason(),
                competitionInfo,
                playersSingleMatchJPA.getMatchDayNumber(),
                playersSingleMatchJPA.getUniqueRowMatchId()
        );
    }
}
