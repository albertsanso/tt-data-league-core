package org.cttelsamicsterrassa.data.core.repository.jpa.match.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.match.model.PlayersSingleMatchJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayersSingleMatchRepositoryHelper extends JpaRepository<PlayersSingleMatchJPA, UUID>, JpaSpecificationExecutor<PlayersSingleMatchJPA> {
    Optional<PlayersSingleMatchJPA> findBySeasonPlayerResultLocal_IdAndSeasonPlayerResultVisitor_IdAndUniqueRowMatchId(UUID seasonPlayerResultLocalId, UUID seasonPlayerResultVisitorId, String uniqueRowMatchId);
    List<PlayersSingleMatchJPA> findBySeasonAndCompetitionTypeAndCompetitionCategoryAndCompetitionScopeAndCompetitionScopeTagAndCompetitionGroupAndMatchDayNumber(
            String season, String competitionType, String competitionCategory, String competitionScope, String competitionScopeTag, String competitionGroup, int matchDayNumber);
}
