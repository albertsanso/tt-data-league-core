package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayer;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface SeasonPlayerRepository {
    Optional<SeasonPlayer> findById(UUID id);
    Optional<SeasonPlayer> findByPracticionerIdClubIdSeason(UUID practicionerId, UUID clubId, String season);
    Optional<SeasonPlayer> findByPracticionerNameAndClubNameAndSeason(String practicionerName, String clubName, String season);
    List<SeasonPlayer> findBySimilarName(String name);
    List<SeasonPlayer> findBySimilarNames(List<String> nameFragments);
    void save(SeasonPlayer seasonPlayer);
}
