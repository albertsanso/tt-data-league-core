package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl;


import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayer;
import org.cttelsamicsterrassa.data.core.domain.repository.SeasonPlayerRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerJPAToSeasonPlayerMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerToSeasonPlayerJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerJPA;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.criteria.Predicate;
import java.util.stream.Collectors;

@Transactional
@Component
public class SeasonPlayerRepositoryImpl implements SeasonPlayerRepository {

    private final SeasonPlayerRepositoryHelper seasonPlayerRepositoryHelper;
    private final SeasonPlayerJPAToSeasonPlayerMapper seasonPlayerJPAToSeasonPlayerMapper;
    private final SeasonPlayerToSeasonPlayerJPAMapper seasonPlayerToSeasonPlayerJPAMapper;

    @Autowired
    public SeasonPlayerRepositoryImpl(SeasonPlayerRepositoryHelper seasonPlayerRepositoryHelper, SeasonPlayerJPAToSeasonPlayerMapper seasonPlayerJPAToSeasonPlayerMapper, SeasonPlayerToSeasonPlayerJPAMapper seasonPlayerToSeasonPlayerJPAMapper) {
        this.seasonPlayerRepositoryHelper = seasonPlayerRepositoryHelper;
        this.seasonPlayerJPAToSeasonPlayerMapper = seasonPlayerJPAToSeasonPlayerMapper;
        this.seasonPlayerToSeasonPlayerJPAMapper = seasonPlayerToSeasonPlayerJPAMapper;
    }

    @Override
    public Optional<SeasonPlayer> findById(UUID id) {
        return seasonPlayerRepositoryHelper.findById(id)
                .map(seasonPlayerJPAToSeasonPlayerMapper);
    }

    @Override
    public Optional<SeasonPlayer> findByPracticionerIdClubIdSeason(UUID practicionerId, UUID clubId, String season) {
        return seasonPlayerRepositoryHelper.findByClubMember_Practicioner_IdAndClubMember_Club_IdAndYearRange(practicionerId, clubId, season)
                .map(seasonPlayerJPAToSeasonPlayerMapper);
    }

    public Optional<SeasonPlayer> findByPracticionerNameAndClubNameAndSeason(String practicionerName, String clubName, String season) {
        return seasonPlayerRepositoryHelper.findByClubMember_Practicioner_FullNameAndClubMember_Club_NameAndYearRange(practicionerName, clubName, season)
                .map(seasonPlayerJPAToSeasonPlayerMapper);
    }

    @Override
    public List<SeasonPlayer> findBySimilarName(String name) {
        return seasonPlayerRepositoryHelper.findByClubMember_Practicioner_FullNameContainingIgnoreCase(name)
                .stream()
                .map(seasonPlayerJPAToSeasonPlayerMapper)
                .toList();
    }

    @Override
    public List<SeasonPlayer> findBySimilarNames(List<String> nameFragments) {
        if (nameFragments == null || nameFragments.isEmpty()) {
            return List.of();
        }

        // Build specification that ORs predicates like lower(practicioner.fullName) like %fragment%
        Specification<SeasonPlayerJPA> spec = (root, query, cb) -> {
            List<Predicate> predicates = nameFragments.stream()
                    .filter(f -> f != null && !f.isBlank())
                    .map(f -> cb.like(cb.lower(root.get("clubMember").get("practicioner").get("fullName")), "%" + f.toLowerCase() + "%"))
                    .toList();

            if (predicates.isEmpty()) return cb.conjunction();

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<SeasonPlayer> mapped = seasonPlayerRepositoryHelper.findAll(spec)
                .stream()
                .map(seasonPlayerJPAToSeasonPlayerMapper)
                .toList();

        // Deduplicate by id preserving order
        List<SeasonPlayer> results = new ArrayList<>();
        Set<UUID> seen = new HashSet<>();
        for (SeasonPlayer sp : mapped) {
            if (sp == null || sp.getId() == null) continue;
            if (seen.add(sp.getId())) results.add(sp);
        }

        return results;
    }

    public void save(SeasonPlayer seasonPlayer) {
        seasonPlayerRepositoryHelper.save(seasonPlayerToSeasonPlayerJPAMapper.apply(seasonPlayer));
    }

}