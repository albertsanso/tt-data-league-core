package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayersSingleMatchRepository {
    Optional<PlayersSingleMatch> findById(UUID id);
    Optional<PlayersSingleMatch> findBySeasonPlayerResultLocalIdAndSeasonPlayerResultVisitorIdAndUniqueId(UUID seasonPlayerResultLocalId, UUID seasonPlayerResultVisitorId, String uniqueId);
    List<PlayersSingleMatch> findBySeasonAndCompetitionAndMatchDayNumber(String season, CompetitionInfo competitionInfo, int matchDayNumber);
    List<PlayersSingleMatch> findAll();
    void save(PlayersSingleMatch playerSingleMatch);
}
