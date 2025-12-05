package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper;


import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayerResult;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerResultJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;

@Component
public class SeasonPlayerResultToSeasonPlayerResultJPAMapper implements Function<SeasonPlayerResult, SeasonPlayerResultJPA> {
    private final SeasonPlayerToSeasonPlayerJPAMapper seasonPlayerToSeasonPlayerJPAMapper;

    @Autowired
    public SeasonPlayerResultToSeasonPlayerResultJPAMapper(SeasonPlayerToSeasonPlayerJPAMapper seasonPlayerToSeasonPlayerJPAMapper) {
        this.seasonPlayerToSeasonPlayerJPAMapper = seasonPlayerToSeasonPlayerJPAMapper;
    }

    @Override
    public SeasonPlayerResultJPA apply(SeasonPlayerResult seasonPlayerResult) {
        SeasonPlayerResultJPA resultJpa = new SeasonPlayerResultJPA();
        resultJpa.setId(seasonPlayerResult.getId());
        resultJpa.setSeason(seasonPlayerResult.getSeason());

        resultJpa.setCompetitionType(seasonPlayerResult.getCompetitionType());
        resultJpa.setCompetitionCategory(seasonPlayerResult.getCompetitionCategory());
        resultJpa.setCompetitionScope(seasonPlayerResult.getCompetitionScope());
        resultJpa.setCompetitionScopeTag(seasonPlayerResult.getCompetitionScopeTag());
        resultJpa.setCompetitionGroup(seasonPlayerResult.getCompetitionGroup());

        resultJpa.setMatchDayNumber(seasonPlayerResult.getMatchInfo().matchDayNumber());
        resultJpa.setMatchDay(seasonPlayerResult.getMatchInfo().matchDay());
        resultJpa.setMatchPlayerLetter(seasonPlayerResult.getMatchInfo().playerLetter());
        resultJpa.setMatchLinkageId(seasonPlayerResult.getMatchInfo().matchLinkageId());
        resultJpa.setMatchGamePoints(Arrays.stream(seasonPlayerResult.getGamePoints())
                .mapToObj(String::valueOf).toList());
        resultJpa.setMatchGamesWon(seasonPlayerResult.getGamesWon());

        resultJpa.setSeasonPlayer(seasonPlayerToSeasonPlayerJPAMapper.apply(seasonPlayerResult.getSeasonPlayer()));

        return resultJpa;
    }
}