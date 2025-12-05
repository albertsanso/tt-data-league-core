package org.cttelsamicsterrassa.data.core.repository.jpa.club.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.Club;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.model.ClubJPA;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Component
public class ClubToClubJPAMapper implements Function<Club, ClubJPA> {
    @Override
    public ClubJPA apply(Club club) {
        ClubJPA clubJpa =  new ClubJPA();
        clubJpa.setId(club.getId());
        clubJpa.setName(club.getName());

        List<String> sortedRanges = club.getYearRanges().stream()
                .sorted(Comparator.comparing(r -> r.split("-")[0])) // sort by first year
                .toList();
        clubJpa.setYearRanges(sortedRanges);

        return clubJpa;
    }
}