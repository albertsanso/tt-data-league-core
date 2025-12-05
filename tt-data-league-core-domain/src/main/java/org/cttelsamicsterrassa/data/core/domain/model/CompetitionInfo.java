package org.cttelsamicsterrassa.data.core.domain.model;

public record CompetitionInfo(
        String competitionType,
        String competitionCategory,
        String competitionScope,
        String competitionScopeTag,
        String competitionGroup
) {
}
