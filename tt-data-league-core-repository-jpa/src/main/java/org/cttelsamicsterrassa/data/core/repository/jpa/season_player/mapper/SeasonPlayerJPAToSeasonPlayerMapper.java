package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.License;
import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayer;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.mapper.ClubMemberJPAToClubMemberMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SeasonPlayerJPAToSeasonPlayerMapper implements Function<SeasonPlayerJPA, SeasonPlayer> {

    private final ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper;

    @Autowired
    public SeasonPlayerJPAToSeasonPlayerMapper(ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper) {
        this.clubMemberJPAToClubMemberMapper = clubMemberJPAToClubMemberMapper;
    }

    @Override
    public SeasonPlayer apply(SeasonPlayerJPA seasonPlayerJPA) {
        return SeasonPlayer.createExisting(
                seasonPlayerJPA.getId(),
                clubMemberJPAToClubMemberMapper.apply(seasonPlayerJPA.getClubMember()),
                new License(seasonPlayerJPA.getLicenseTag(), seasonPlayerJPA.getLicenseRef()),
                seasonPlayerJPA.getYearRange()
        );
    }
}