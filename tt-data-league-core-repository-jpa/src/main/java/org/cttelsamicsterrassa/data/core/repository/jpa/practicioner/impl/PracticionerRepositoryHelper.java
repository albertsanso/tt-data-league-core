package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PracticionerRepositoryHelper extends JpaRepository<PracticionerJPA, UUID> {
    Optional<PracticionerJPA> findByFullName(String fullName);
}