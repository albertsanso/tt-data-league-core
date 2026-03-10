package org.cttelsamicsterrassa.data.core.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class PlayersSingleMatch {

    private final UUID id;

    private final SeasonPlayerResult seasonPlayerResultLocal;

    private final SeasonPlayerResult seasonPlayerResultVisitor;

    private final String season;

    private final CompetitionInfo competitionInfo;

    private final int matchDayNumber;

    private final String uniqueRowMatchId;

    private final ZonedDateTime matchDateTime;

    private PlayersSingleMatch(
            UUID id,
            SeasonPlayerResult seasonPlayerResultLocal,
            SeasonPlayerResult seasonPlayerResultVisitor,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String uniqueRowMatchId,
            ZonedDateTime matchDateTime) {
        this.id = id;
        this.seasonPlayerResultLocal = seasonPlayerResultLocal;
        this.seasonPlayerResultVisitor = seasonPlayerResultVisitor;
        this.season = season;
        this.competitionInfo = competitionInfo;
        this.matchDayNumber = matchDayNumber;
        this.uniqueRowMatchId = uniqueRowMatchId;
        this.matchDateTime = matchDateTime;
    }


    public static PlayersSingleMatch createNew(
            SeasonPlayerResult seasonPlayerResultLocal,
            SeasonPlayerResult seasonPlayerResultVisitor,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String uniqueRowMatchId,
            ZonedDateTime matchDateTime) {
        return new PlayersSingleMatch(
                UUID.randomUUID(),
                seasonPlayerResultLocal,
                seasonPlayerResultVisitor,
                season,
                competitionInfo,
                matchDayNumber,
                uniqueRowMatchId,
                matchDateTime);
    }

    public static PlayersSingleMatch createExisting(
            UUID id,
            SeasonPlayerResult seasonPlayerResultLocal,
            SeasonPlayerResult seasonPlayerResultVisitor,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String uniqueRowMatchId,
            ZonedDateTime matchDateTime) {
        return new PlayersSingleMatch(
                id,
                seasonPlayerResultLocal,
                seasonPlayerResultVisitor,
                season,
                competitionInfo,
                matchDayNumber,
                uniqueRowMatchId,
                matchDateTime);
    }

    public UUID getId() {
        return id;
    }

    public SeasonPlayerResult getSeasonPlayerResultLocal() {
        return seasonPlayerResultLocal;
    }

    public SeasonPlayerResult getSeasonPlayerResultVisitor() {
        return seasonPlayerResultVisitor;
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

    public ZonedDateTime getMatchDateTime() {
        return matchDateTime;
    }
}
