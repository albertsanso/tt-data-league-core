package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PracticionerRepositoryHelper extends JpaRepository<PracticionerJPA, UUID>, JpaSpecificationExecutor<PracticionerJPA> {
    Optional<PracticionerJPA> findByFullName(String fullName);
}