package org.cttelsamicsterrassa.data.core.repository.jpa.club.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.club.model.ClubJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClubRepositoryHelper extends JpaRepository<ClubJPA, UUID> {
    Optional<ClubJPA> findByName(String name);
    boolean existsByName(String name);
}
