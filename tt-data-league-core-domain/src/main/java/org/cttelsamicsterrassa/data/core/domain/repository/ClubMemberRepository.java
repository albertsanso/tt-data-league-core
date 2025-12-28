package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.ClubMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClubMemberRepository {
    Optional<ClubMember> findById(UUID id);
    Optional<ClubMember> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
    boolean existsByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
    List<ClubMember> findByClubId(UUID clubId);
    List<ClubMember> findByPracticionerId(UUID practicionerId);
    void save(ClubMember clubMember);
}
