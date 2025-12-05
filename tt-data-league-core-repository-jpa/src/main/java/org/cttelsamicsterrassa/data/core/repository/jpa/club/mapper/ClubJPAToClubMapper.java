package org.cttelsamicsterrassa.data.core.repository.jpa.club.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.Club;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.model.ClubJPA;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.function.Function;

@Component
public class ClubJPAToClubMapper implements Function<ClubJPA, Club> {
    @Override
    public Club apply(ClubJPA clubJPA) {
        Club club = Club.createExisting(clubJPA.getId(), clubJPA.getName());

        clubJPA.getYearRanges().stream()
                .sorted(Comparator.comparing(r -> r.split("-")[0]))
                .forEach(club::addYearRange);

        return club;
    }
}