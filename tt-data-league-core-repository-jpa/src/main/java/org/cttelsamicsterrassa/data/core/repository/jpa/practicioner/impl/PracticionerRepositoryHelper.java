package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PracticionerRepositoryHelper extends JpaRepository<PracticionerJPA, String> {
    Optional<PracticionerJPA> findByFullName(String fullName);
}