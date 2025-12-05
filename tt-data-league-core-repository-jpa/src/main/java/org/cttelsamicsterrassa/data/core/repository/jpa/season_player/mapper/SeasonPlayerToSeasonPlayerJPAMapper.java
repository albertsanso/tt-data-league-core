package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.SeasonPlayer;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.mapper.ClubMemberToClubMemberJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SeasonPlayerToSeasonPlayerJPAMapper implements Function<SeasonPlayer, SeasonPlayerJPA> {
    private final ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper;

    @Autowired
    public SeasonPlayerToSeasonPlayerJPAMapper(ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper) {
        this.clubMemberToClubMemberJPAMapper = clubMemberToClubMemberJPAMapper;
    }

    @Override
    public SeasonPlayerJPA apply(SeasonPlayer seasonPlayer) {
        SeasonPlayerJPA seasonPlayerJPA = new SeasonPlayerJPA();
        seasonPlayerJPA.setId(seasonPlayer.getId());
        seasonPlayerJPA.setClubMember(clubMemberToClubMemberJPAMapper.apply(seasonPlayer.getClubMember()));
        seasonPlayerJPA.setLicenseTag(seasonPlayer.getLicense().tag());
        seasonPlayerJPA.setLicenseRef(seasonPlayer.getLicense().id());
        seasonPlayerJPA.setYearRange(seasonPlayer.getYearRange());
        return seasonPlayerJPA;
    }
}