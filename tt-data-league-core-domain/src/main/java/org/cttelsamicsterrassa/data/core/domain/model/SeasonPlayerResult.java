package org.cttelsamicsterrassa.data.core.domain.model;

import java.util.UUID;

public class SeasonPlayerResult {

    private final UUID id;
    private final String season;
    private final SeasonPlayer seasonPlayer;
    private final CompetitionInfo competitionInfo;
    private final MatchInfo matchInfo;
    private final TeamRole teamRole;

    private SeasonPlayerResult(UUID id, String season, CompetitionInfo competitionInfo, SeasonPlayer seasonPlayer, MatchInfo matchInfo, TeamRole teamRole) {
        this.id = id;
        this.season = season;
        this.competitionInfo = competitionInfo;
        this.matchInfo = matchInfo;
        this.seasonPlayer = seasonPlayer;
        this.teamRole = teamRole;
    }

    public static SeasonPlayerResult createNew(
            String season,
            CompetitionInfo competitionInfo,
            SeasonPlayer seasonPlayer,
            MatchInfo matchInfo,
            TeamRole teamRole
) {
        return new SeasonPlayerResult(
                UUID.randomUUID(),
                season,
                competitionInfo,
                seasonPlayer,
                matchInfo, teamRole);
    }

    public static SeasonPlayerResult createExisting(
            UUID id,
            String season,
            CompetitionInfo competitionInfo,
            SeasonPlayer seasonPlayer,
            MatchInfo matchInfo,
            TeamRole teamRole) {
        return new SeasonPlayerResult(
                id,
                season,
                competitionInfo,
                seasonPlayer,
                matchInfo, teamRole);
    }

    public UUID getId() {
        return id;
    }

    public String getSeason() {
        return season;
    }

    public String getCompetitionType() {
        return competitionInfo.competitionType();
    }

    public String getCompetitionCategory() {
        return competitionInfo.competitionCategory();
    }

    public int getMatchDayNumber() {
        return matchInfo.matchDayNumber();
    }

    public SeasonPlayer getSeasonPlayer() {
        return seasonPlayer;
    }

    public String getPlayerLetter() {
        return matchInfo.playerLetter();
    }

    public int[] getGamePoints() {
        return matchInfo.gamePoints();
    }

    public int getGamesWon() {
        return matchInfo.gamesWon();
    }

    public String getMatchLinkageId() {
        return matchInfo.matchLinkageId();
    }

    public String getMatchDay() {
        return matchInfo.matchDay();
    }

    public String getCompetitionScope() {
        return competitionInfo.competitionScope();
    }

    public String getCompetitionScopeTag() {
        return competitionInfo.competitionScopeTag();
    }

    public String getCompetitionGroup() {
        return competitionInfo.competitionGroup();
    }

    public String getCompetitionGender() {
        return competitionInfo.competitionGender();
    }
    public CompetitionInfo getCompetitionInfo() {
        return competitionInfo;
    }

    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    public TeamRole getTeamRole() {
        return teamRole;
    }
}
