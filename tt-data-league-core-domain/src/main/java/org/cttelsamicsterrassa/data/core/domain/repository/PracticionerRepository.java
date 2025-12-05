package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.Practicioner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PracticionerRepository {
    Optional<Practicioner> findById(UUID id);
    Optional<Practicioner> findByFullName(String fullName);
    List<Practicioner> findAll();
    void save(Practicioner practicioner);
}
