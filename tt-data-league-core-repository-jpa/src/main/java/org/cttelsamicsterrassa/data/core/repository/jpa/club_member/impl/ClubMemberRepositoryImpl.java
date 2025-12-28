package org.cttelsamicsterrassa.data.core.repository.jpa.club_member.impl;


import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.ClubMember;
import org.cttelsamicsterrassa.data.core.domain.repository.ClubMemberRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.mapper.ClubMemberJPAToClubMemberMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.mapper.ClubMemberToClubMemberJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class ClubMemberRepositoryImpl implements ClubMemberRepository {

    private final ClubMemberRepositoryHelper clubMemberRepositoryHelper;

    private final ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper;

    private final ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper;

    @Autowired
    public ClubMemberRepositoryImpl(ClubMemberRepositoryHelper clubMemberRepositoryHelper, ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper, ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper) {
        this.clubMemberRepositoryHelper = clubMemberRepositoryHelper;
        this.clubMemberJPAToClubMemberMapper = clubMemberJPAToClubMemberMapper;
        this.clubMemberToClubMemberJPAMapper = clubMemberToClubMemberJPAMapper;
    }

    @Override
    public Optional<ClubMember> findById(UUID id) {
        return clubMemberRepositoryHelper.findById(id).map(clubMemberJPAToClubMemberMapper);
    }

    @Override
    public Optional<ClubMember> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId) {
        return clubMemberRepositoryHelper.findByPracticionerIdAndClubId(practicionerId, clubId).map(clubMemberJPAToClubMemberMapper);
    }

    @Override
    public boolean existsByPracticionerIdAndClubId(UUID practicionerId, UUID clubId) {
        return clubMemberRepositoryHelper.findByPracticionerIdAndClubId(practicionerId, clubId).isPresent();
    }

    @Override
    public List<ClubMember> findByClubId(UUID clubId) {
        return clubMemberRepositoryHelper.findByClubId(clubId)
                .stream()
                .map(clubMemberJPAToClubMemberMapper)
                .toList();
    }

    @Override
    public List<ClubMember> findByPracticionerId(UUID practicionerId) {
        return clubMemberRepositoryHelper.findByPracticionerId(practicionerId)
                .stream()
                .map(clubMemberJPAToClubMemberMapper)
                .toList();
    }

    @Override
    public void save(ClubMember clubMember) {
        clubMemberRepositoryHelper.save(clubMemberToClubMemberJPAMapper.apply(clubMember));
    }
}