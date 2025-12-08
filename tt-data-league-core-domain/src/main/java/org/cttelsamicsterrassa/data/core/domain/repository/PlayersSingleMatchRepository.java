package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayersSingleMatchRepository {
    Optional<PlayersSingleMatch> findById(UUID id);
    Optional<PlayersSingleMatch> findBySeasonPlayerResultAbcIdAndSeasonPlayerResultXyzIdAndUniqueId(UUID seasonPlayerResultAbcId, UUID seasonPlayerResultXyzId, String uniqueId);
    List<PlayersSingleMatch> findAll();
    void save(PlayersSingleMatch playerSingleMatch);
}
