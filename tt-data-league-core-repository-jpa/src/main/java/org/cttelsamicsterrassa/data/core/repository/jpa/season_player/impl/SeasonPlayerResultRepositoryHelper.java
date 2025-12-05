package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerResultJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonPlayerResultRepositoryHelper extends JpaRepository<SeasonPlayerResultJPA, UUID>
{
    Optional<SeasonPlayerResultJPA> findBySeasonAndCompetitionAndJornadaAndGroupAndPlayerLetterAndMatchLinkageIdAndSeasonPlayer_ClubMember_Club_Id(
            String season,
            String competition,
            String jornada,
            String group,
            String playerLetter,
            String matchLinkageId,
            UUID clubId);
}
