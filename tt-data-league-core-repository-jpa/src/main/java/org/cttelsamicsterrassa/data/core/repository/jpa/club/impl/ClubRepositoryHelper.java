package org.cttelsamicsterrassa.data.core.repository.jpa.club.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.club.model.ClubJPA;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepositoryHelper extends CrudRepository<ClubJPA, String> {
    Optional<ClubJPA> findByName(String name);
    boolean existsByName(String name);
}
