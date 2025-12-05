package org.cttelsamicsterrassa.data.core.repository.jpa.club_member.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.ClubMember;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.mapper.ClubJPAToClubMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.model.ClubMemberJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.mapper.PracticionerJPAToPracticionerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.function.Function;

@Component
public class ClubMemberJPAToClubMemberMapper implements Function<ClubMemberJPA, ClubMember> {

    private final ClubJPAToClubMapper clubJPAToClubMapper;

    private final PracticionerJPAToPracticionerMapper practicionerJPAToPracticionerMapper;

    @Autowired
    public ClubMemberJPAToClubMemberMapper(ClubJPAToClubMapper clubJPAToClubMapper, PracticionerJPAToPracticionerMapper practicionerJPAToPracticionerMapper) {
        this.clubJPAToClubMapper = clubJPAToClubMapper;
        this.practicionerJPAToPracticionerMapper = practicionerJPAToPracticionerMapper;
    }

    @Override
    public ClubMember apply(ClubMemberJPA clubMemberJPA) {
        ClubMember clubMember = ClubMember.createExisting(
                clubMemberJPA.getId(),
                clubJPAToClubMapper.apply(clubMemberJPA.getClub()),
                practicionerJPAToPracticionerMapper.apply(clubMemberJPA.getPracticioner())
        );

        clubMemberJPA.getYearRanges().stream()
                .sorted(Comparator.comparing(r -> r.split("-")[0]))
                .forEach(clubMember::addYearRange);

        return clubMember;
    }
}