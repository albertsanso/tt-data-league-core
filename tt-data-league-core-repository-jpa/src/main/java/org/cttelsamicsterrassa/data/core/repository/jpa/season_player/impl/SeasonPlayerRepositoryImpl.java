package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl;


import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayer;
import org.cttelsamicsterrassa.data.core.domain.repository.SeasonPlayerRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerJPAToSeasonPlayerMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerToSeasonPlayerJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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
    public Optional<SeasonPlayer> findById(String id) {
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

    public void save(SeasonPlayer seasonPlayer) {
        seasonPlayerRepositoryHelper.save(seasonPlayerToSeasonPlayerJPAMapper.apply(seasonPlayer));
    }
}