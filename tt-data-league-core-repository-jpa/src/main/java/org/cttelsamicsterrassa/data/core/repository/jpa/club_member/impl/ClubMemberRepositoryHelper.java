package org.cttelsamicsterrassa.data.core.repository.jpa.club_member.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.model.ClubMemberJPA;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClubMemberRepositoryHelper extends CrudRepository<ClubMemberJPA, String> {
    Optional<ClubMemberJPA> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
}
