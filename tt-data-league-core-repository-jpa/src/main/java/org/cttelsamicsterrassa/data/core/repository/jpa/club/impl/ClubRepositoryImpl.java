package org.cttelsamicsterrassa.data.core.repository.jpa.club.impl;

import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.Club;
import org.cttelsamicsterrassa.data.core.domain.repository.ClubRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.mapper.ClubJPAToClubMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.mapper.ClubToClubJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class ClubRepositoryImpl implements ClubRepository {

    private final ClubRepositoryHelper clubRepositoryHelper;
    private final ClubJPAToClubMapper clubJPAToClubMapper;
    private final ClubToClubJPAMapper clubToClubJPAMapper;

    @Autowired
    public ClubRepositoryImpl(ClubRepositoryHelper clubRepositoryHelper, ClubJPAToClubMapper clubJPAToClubMapper, ClubToClubJPAMapper clubToClubJPAMapper) {
        this.clubRepositoryHelper = clubRepositoryHelper;
        this.clubJPAToClubMapper = clubJPAToClubMapper;
        this.clubToClubJPAMapper = clubToClubJPAMapper;
    }

    @Override
    public Optional<Club> findById(UUID id) {
        return clubRepositoryHelper.findById(id)
                .map(clubJPAToClubMapper);
    }

    @Override
    public Optional<Club> findByName(String name) {
        return clubRepositoryHelper.findByName(name)
                .map(clubJPAToClubMapper);
    }

    @Override
    public List<Club> findAll() {
        List<Club> clubList = new ArrayList<>();
        clubRepositoryHelper.findAll().forEach(clubJPA -> {
            clubList.add(clubJPAToClubMapper.apply(clubJPA));
        });
        return clubList;
    }

    @Override
    public boolean existsById(UUID id) {
        return clubRepositoryHelper.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return clubRepositoryHelper.existsByName(name);
    }

    @Override
    public void deteleById(UUID id) {
        clubRepositoryHelper.deleteById(id);
    }

    @Override
    public void save(Club club) {
        clubRepositoryHelper.save(clubToClubJPAMapper.apply(club));
    }
}
