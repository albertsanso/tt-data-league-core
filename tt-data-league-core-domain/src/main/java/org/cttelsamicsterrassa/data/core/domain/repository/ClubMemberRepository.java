package org.cttelsamicsterrassa.data.core.domain.repository;

import org.cttelsamicsterrassa.data.core.domain.model.ClubMember;

import java.util.Optional;
import java.util.UUID;

public interface ClubMemberRepository {
    Optional<ClubMember> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
    void save(ClubMember clubMember);
}
