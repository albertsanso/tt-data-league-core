package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonPlayerRepositoryHelper extends JpaRepository<SeasonPlayerJPA, UUID>, JpaSpecificationExecutor<SeasonPlayerJPA> {
    Optional<SeasonPlayerJPA> findByClubMember_Practicioner_IdAndClubMember_Club_IdAndYearRange(
            UUID practicionerId,
            UUID clubId,
            String yearRange
    );

    Optional<SeasonPlayerJPA> findByClubMember_Practicioner_FullNameAndClubMember_Club_NameAndYearRange(
            String practicionerName,
            String clubName,
            String yearRange
    );

    List<SeasonPlayerJPA> findByClubMember_Practicioner_FullNameContainingIgnoreCase(
            String practicionerName
    );
}
