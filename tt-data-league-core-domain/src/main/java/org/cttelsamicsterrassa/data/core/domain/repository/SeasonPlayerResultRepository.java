package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayerResult;

import java.util.Optional;
import java.util.UUID;

public interface SeasonPlayerResultRepository {
    Optional<SeasonPlayerResult> findById(UUID id);
    Optional<SeasonPlayerResult> findFor(
            String season,
            String competition,
            String jornada,
            String group,
            String playerLetter,
            String matchLinkageId,
            UUID clubId
    );
    void save(SeasonPlayerResult seasonPlayerResult);
}
