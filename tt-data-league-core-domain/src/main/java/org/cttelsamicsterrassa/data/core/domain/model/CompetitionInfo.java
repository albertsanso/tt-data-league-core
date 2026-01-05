package org.cttelsamicsterrassa.data.core.domain.model;

public record CompetitionInfo(
        String competitionType,
        String competitionCategory,
        String competitionScope,
        String competitionScopeTag,
        String competitionGroup,
        String competitionGender
) {
    public CompetitionInfo createCopy() {
        return new CompetitionInfo(
                this.competitionType,
                this.competitionCategory,
                this.competitionScope,
                this.competitionScopeTag,
                this.competitionGroup,
                this.competitionGender
        );
    }
}
