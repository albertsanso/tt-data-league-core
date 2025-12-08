package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerResultJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonPlayerResultRepositoryHelper extends JpaRepository<SeasonPlayerResultJPA, UUID>
{
    Optional<SeasonPlayerResultJPA> findBySeasonAndCompetitionTypeAndCompetitionCategoryAndCompetitionScopeAndCompetitionScopeTagAndCompetitionGroupAndMatchDayNumberAndMatchPlayerLetterAndMatchLinkageIdAndSeasonPlayer_ClubMember_Club_Id(
            String season, String competitionType, String competitionCategory, String competitionScope, String competitionScopeTag, String competitionGroup,
            int matchDayNumber, String matchPlayerLetter, String matchLinkageId, UUID clubId
    );
}
