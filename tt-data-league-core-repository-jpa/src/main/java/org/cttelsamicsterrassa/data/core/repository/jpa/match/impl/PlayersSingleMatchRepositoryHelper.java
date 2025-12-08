package org.cttelsamicsterrassa.data.core.repository.jpa.match.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.match.model.PlayersSingleMatchJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayersSingleMatchRepositoryHelper extends JpaRepository<PlayersSingleMatchJPA, UUID> {
    Optional<PlayersSingleMatchJPA> findBySeasonPlayerResultAbc_IdAndSeasonPlayerResultXyz_IdAndUniqueRowMatchId(UUID seasonPlayerResultAbcId, UUID seasonPlayerResultXyzId, String uniqueRowMatchId);
}
