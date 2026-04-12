package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayerResult;
import org.cttelsamicsterrassa.data.core.domain.model.TeamRole;

import java.util.Optional;
import java.util.UUID;

public interface SeasonPlayerResultRepository {
    Optional<SeasonPlayerResult> findById(UUID id);
    Optional<SeasonPlayerResult> findFor(
            String season,
            String competitionType,
            String competitionCategory,
            String competitionScope,
            String competitionScopeTag,
            String competitionGroup,
            int matchDayNumber,
            String matchPlayerLetter,
            String matcPlayersPairing,
            TeamRole matchPlayerRole,
            UUID clubId
    );
    public void deteleById(UUID id);
    void save(SeasonPlayerResult seasonPlayerResult);
}
