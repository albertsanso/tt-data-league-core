package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl;

import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayerResult;
import org.cttelsamicsterrassa.data.core.domain.repository.SeasonPlayerResultRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerResultJPAToSeasonPlayerResultMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper.SeasonPlayerResultToSeasonPlayerResultJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class SeasonPlayerResultRepositoryImpl implements SeasonPlayerResultRepository {

    private final SeasonPlayerResultRepositoryHelper helper;

    private final SeasonPlayerResultToSeasonPlayerResultJPAMapper toJPAMapper;

    private final SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper;

    @Autowired
    public SeasonPlayerResultRepositoryImpl(SeasonPlayerResultRepositoryHelper helper, SeasonPlayerResultToSeasonPlayerResultJPAMapper toJPAMapper, SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper) {
        this.helper = helper;
        this.toJPAMapper = toJPAMapper;
        this.fromJPAMapper = fromJPAMapper;
    }

    @Override
    public Optional<SeasonPlayerResult> findById(UUID id) {
        return helper.findById(id).map(fromJPAMapper);
    }

    @Override
    public Optional<SeasonPlayerResult> findFor(
            String season,
            String competitionType,
            String competitionCategory,
            String competitionScope,
            String competitionScopeTag,
            String competitionGroup,
            int matchDayNumber,
            String matchPlayerLetter,
            String matchLinkageId,
            UUID clubId
    ) {
        return helper.findBySeasonAndCompetitionTypeAndCompetitionCategoryAndCompetitionScopeAndCompetitionScopeTagAndCompetitionGroupAndMatchDayNumberAndMatchPlayerLetterAndMatchLinkageIdAndSeasonPlayer_ClubMember_Club_Id(
            season,
            competitionType,
            competitionCategory,
            competitionScope,
            competitionScopeTag,
            competitionGroup,
            matchDayNumber,
            matchPlayerLetter,
            matchLinkageId,
            clubId
        ).map(fromJPAMapper);
    }

    @Override
    public Optional<SeasonPlayerResult> findByUniqueKey(String uniqueKey) {
        return helper.findByMatchLinkageId(uniqueKey).map(fromJPAMapper);
    }

    @Override
    public void save(SeasonPlayerResult seasonPlayerResult) {
        helper.save(toJPAMapper.apply(seasonPlayerResult));
    }
}