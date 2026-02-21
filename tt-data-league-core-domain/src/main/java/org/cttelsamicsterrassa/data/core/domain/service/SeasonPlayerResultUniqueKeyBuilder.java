package org.cttelsamicsterrassa.data.core.domain.service;

import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayer;

public class SeasonPlayerResultUniqueKeyBuilder {

    public static String buildUniqueKey(SeasonPlayer seasonPlayer, CompetitionInfo competitionInfo, int matchDayNumber, String matchPlayerLetter) {
        return "%s-%s-%s-%d-%s-%s-%s".formatted(
                seasonPlayer.getYearRange(),
                competitionInfo.competitionType(),
                competitionInfo.competitionCategory(),
                matchDayNumber,
                competitionInfo.competitionGroup(),
                seasonPlayer.getLicense(),
                matchPlayerLetter
        );
    }
}
