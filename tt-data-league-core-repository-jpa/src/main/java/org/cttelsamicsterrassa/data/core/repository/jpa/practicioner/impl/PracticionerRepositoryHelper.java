package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PracticionerRepositoryHelper extends CrudRepository<PracticionerJPA, String> {
    Optional<PracticionerJPA> findByFullName(String fullName);
}