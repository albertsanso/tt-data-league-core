package org.cttelsamicsterrassa.data.core.repository.jpa.club_member.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.model.ClubMemberJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClubMemberRepositoryHelper extends JpaRepository<ClubMemberJPA, String> {
    Optional<ClubMemberJPA> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
    List<ClubMemberJPA> findByClubId(UUID clubId);
    List<ClubMemberJPA> findByPracticionerId(UUID practicionerId);
}
