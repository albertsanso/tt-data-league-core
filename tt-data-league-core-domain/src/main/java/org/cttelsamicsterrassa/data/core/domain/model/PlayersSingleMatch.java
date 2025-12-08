package org.cttelsamicsterrassa.data.core.domain.model;

import java.util.UUID;

public class PlayersSingleMatch {

    private final UUID id;

    private final SeasonPlayerResult seasonPlayerResultAbc;

    private final SeasonPlayerResult seasonPlayerResultXyz;

    private final String season;

    private final CompetitionInfo competitionInfo;

    private final int matchDayNumber;

    private final String uniqueRowMatchId;

    private PlayersSingleMatch(
            UUID id,
            SeasonPlayerResult seasonPlayerResultAbc,
            SeasonPlayerResult seasonPlayerResultXyz,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String uniqueRowMatchId) {
        this.id = id;
        this.seasonPlayerResultAbc = seasonPlayerResultAbc;
        this.seasonPlayerResultXyz = seasonPlayerResultXyz;
        this.season = season;
        this.competitionInfo = competitionInfo;
        this.matchDayNumber = matchDayNumber;
        this.uniqueRowMatchId = uniqueRowMatchId;
    }


    public static PlayersSingleMatch createNew(
            SeasonPlayerResult seasonPlayerResultAbc,
            SeasonPlayerResult seasonPlayerResultXyz,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String uniqueRowMatchId) {
        return new PlayersSingleMatch(
                UUID.randomUUID(),
                seasonPlayerResultAbc,
                seasonPlayerResultXyz,
                season,
                competitionInfo,
                matchDayNumber,
                uniqueRowMatchId);
    }

    public static PlayersSingleMatch createExisting(
            UUID id,
            SeasonPlayerResult seasonPlayerResultAbc,
            SeasonPlayerResult seasonPlayerResultXyz,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String uniqueRowMatchId) {
        return new PlayersSingleMatch(
                id,
                seasonPlayerResultAbc,
                seasonPlayerResultXyz,
                season,
                competitionInfo,
                matchDayNumber,
                uniqueRowMatchId);
    }

    public UUID getId() {
        return id;
    }

    public SeasonPlayerResult getSeasonPlayerResultAbc() {
        return seasonPlayerResultAbc;
    }

    public SeasonPlayerResult getSeasonPlayerResultXyz() {
        return seasonPlayerResultXyz;
    }

    public String getSeason() {
        return season;
    }

    public int getMatchDayNumber() {
        return matchDayNumber;
    }

    public String getCompetitionType() {
        return competitionInfo.competitionType();
    }

    public String getCompetitionCategory() {
        return competitionInfo.competitionCategory();
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

    public String getUniqueRowMatchId() {
        return uniqueRowMatchId;
    }
}
