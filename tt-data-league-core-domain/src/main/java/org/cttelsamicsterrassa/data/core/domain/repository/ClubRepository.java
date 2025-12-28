package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.Club;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClubRepository {
    Optional<Club> findById(UUID id);
    Optional<Club> findByName(String name);
    List<Club> findAll();
    boolean existsById(UUID id);
    boolean existsByName(String name);
    List<Club> searchBySimilarName(String name);
    public void deteleById(UUID id);
    void save(Club club);
}
