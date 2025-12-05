package org.cttelsamicsterrassa.data.core.domain.model;

public record MatchInfo(
        int matchDayNumber,
        String matchDay,
        String playerLetter,
        int[] gamePoints,
        int gamesWon,
        String matchLinkageId
) {
}
