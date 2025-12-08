package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.MatchInfo;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayer;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayerResult;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerResultJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SeasonPlayerResultJPAToSeasonPlayerResultMapper implements Function<SeasonPlayerResultJPA, SeasonPlayerResult> {

    private final SeasonPlayerJPAToSeasonPlayerMapper seasonPlayerJPAToSeasonPlayerMapper;

    @Autowired
    public SeasonPlayerResultJPAToSeasonPlayerResultMapper(SeasonPlayerJPAToSeasonPlayerMapper seasonPlayerJPAToSeasonPlayerMapper) {
        this.seasonPlayerJPAToSeasonPlayerMapper = seasonPlayerJPAToSeasonPlayerMapper;
    }

    @Override
    public SeasonPlayerResult apply(SeasonPlayerResultJPA seasonPlayerResultJPA) {
        CompetitionInfo competitionInfo = new CompetitionInfo(
                seasonPlayerResultJPA.getCompetitionType(),
                seasonPlayerResultJPA.getCompetitionCategory(),
                seasonPlayerResultJPA.getCompetitionScope(),
                seasonPlayerResultJPA.getCompetitionScopeTag(),
                seasonPlayerResultJPA.getCompetitionGroup(),
                seasonPlayerResultJPA.getCompetitionGender()
        );

        MatchInfo matchInfo = new MatchInfo(
                seasonPlayerResultJPA.getMatchDayNumber(),
                seasonPlayerResultJPA.getMatchDay(),
                seasonPlayerResultJPA.getMatchPlayerLetter(),
                seasonPlayerResultJPA.getMatchGamePoints().stream().mapToInt(Integer::parseInt).toArray(),
                seasonPlayerResultJPA.getMatchGamesWon(),
                seasonPlayerResultJPA.getMatchLinkageId()
        );

        SeasonPlayer seasonPlayer = seasonPlayerJPAToSeasonPlayerMapper.apply(seasonPlayerResultJPA.getSeasonPlayer());

        return SeasonPlayerResult.createExisting(
                seasonPlayerResultJPA.getId(),
                seasonPlayerResultJPA.getSeason(),
                competitionInfo,
                seasonPlayer,
                matchInfo
        );
    }
}