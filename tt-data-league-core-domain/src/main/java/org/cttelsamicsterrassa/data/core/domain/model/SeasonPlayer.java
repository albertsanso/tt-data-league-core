package org.cttelsamicsterrassa.data.core.domain.model;

import java.util.UUID;

public class SeasonPlayer {

    private final UUID id;
    private final ClubMember clubMember;
    private final License license;
    private String yearRange;

    private SeasonPlayer(UUID id, ClubMember clubMember, License license, String yearRange) {
        this.license = license;
        this.clubMember = clubMember;
        this.id = id;
        this.yearRange = yearRange;
    }

    public static SeasonPlayer createNew(ClubMember clubMember, License license, String yearRange) {
        return new SeasonPlayer(
                UUID.randomUUID(),
                clubMember,
                license,
                yearRange
        );
    }

    public static SeasonPlayer createExisting(UUID id, ClubMember clubMember, License license, String yearRange) {
        return new SeasonPlayer(
                id,
                clubMember,
                license,
                yearRange
        );
    }

    public UUID getId() {
        return id;
    }

    public ClubMember getClubMember() {
        return clubMember;
    }

    public License getLicense() {
        return license;
    }

    public String getYearRange() {
        return yearRange;
    }
}
