package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.Club;

import java.util.List;
import java.util.Optional;

public interface ClubRepository {
    Optional<Club> findById(String id);
    Optional<Club> findByName(String name);
    List<Club> findAll();
    boolean existsById(String id);
    boolean existsByName(String name);
    void save(Club club);
}
