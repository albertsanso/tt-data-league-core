package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.impl;


import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.Practicioner;
import org.cttelsamicsterrassa.data.core.domain.repository.PracticionerRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.mapper.PracticionerJPAToPracticionerMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.mapper.PracticionerToPracticionerJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class PracticionerRepositoryImpl implements PracticionerRepository {
    private static final String INVALID_QUERY_NAME_MESSAGE = "Practicioner name fragment cannot be null or blank";

    private final PracticionerRepositoryHelper practicionerRepositoryHelper;
    private final PracticionerToPracticionerJPAMapper toJPAMapper;
    private final PracticionerJPAToPracticionerMapper fromJPAMapper;

    @Autowired
    public PracticionerRepositoryImpl(PracticionerRepositoryHelper practicionerRepositoryHelper, PracticionerToPracticionerJPAMapper toJPAMapper, PracticionerJPAToPracticionerMapper fromJPAMapper) {
        this.practicionerRepositoryHelper = practicionerRepositoryHelper;
        this.toJPAMapper = toJPAMapper;
        this.fromJPAMapper = fromJPAMapper;
    }

    @Override
    public Optional<Practicioner> findById(UUID id) {
        return practicionerRepositoryHelper.findById(id).map(fromJPAMapper);
    }

    @Override
    public Optional<Practicioner> findByFullName(String fullName) {
        return practicionerRepositoryHelper.findByFullName(fullName).map(fromJPAMapper);
    }

    @Override
    public List<Practicioner> searchBySimilarName(String queryName) {
        if (queryName == null || queryName.isBlank()) {
            throw new IllegalArgumentException(INVALID_QUERY_NAME_MESSAGE);
        }

        List<String> fragments = Arrays.stream(queryName.trim().split("\\s+"))
                .filter(fragment -> !fragment.isBlank())
                .map(fragment -> fragment.toLowerCase(Locale.ROOT))
                .toList();

        Specification<PracticionerJPA> specification = (root, query, cb) -> {
            List<Predicate> predicates = fragments.stream()
                    .map(fragment -> cb.like(cb.lower(root.get("fullName")), "%" + fragment + "%"))
                    .toList();

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return practicionerRepositoryHelper.findAll(specification)
                .stream()
                .map(fromJPAMapper)
                .toList();
    }

    @Override
    public List<Practicioner> findAll() {
        List<Practicioner> practicionerList = new ArrayList<>();
        practicionerRepositoryHelper.findAll().forEach(
                practicionerJPA ->
                        practicionerList.add(fromJPAMapper.apply(practicionerJPA)));
        return practicionerList;
    }

    @Override
    public void deteleById(UUID id) {
        practicionerRepositoryHelper.deleteById(id);
    }

    @Override
    public void save(Practicioner practicioner) {
        practicionerRepositoryHelper.save(toJPAMapper.apply(practicioner));
    }
}
