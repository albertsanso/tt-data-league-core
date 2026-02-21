package org.cttelsamicsterrassa.data.core.domain.model;

import org.albertsanso.commons.model.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ClubMember extends Entity {
    private final UUID id;
    private final Club club;
    private final Practicioner practicioner;
    private List<String> yearRanges = new ArrayList<>();

    private ClubMember(UUID id, Club club, Practicioner practicioner) {
        this.id = id;
        this.club = club;
        this.practicioner = practicioner;
    }

    public static ClubMember createNew(Club club, Practicioner practicioner) {
        return new ClubMember(UUID.randomUUID(), club, practicioner);
    }

    public static ClubMember createExisting(UUID id, Club club, Practicioner practicioner) {
        return new ClubMember(id, club, practicioner);
    }

    public UUID getId() {
        return id;
    }

    public Club getClub() {
        return club;
    }

    public String getFirstName() {
        return practicioner.getFirstName();
    }

    public String getSecondName() {
        return practicioner.getSecondName();
    }

    public String getFullName() {
        return practicioner.getFullName();
    }

    public List<String> getYearRanges() {
        return Collections.unmodifiableList(yearRanges);
    }

    public void addYearRange(String newYearRange) {
        if (!yearRanges.contains(newYearRange)) {
            yearRanges.add(newYearRange);
        }
    }

    public void removeYearRange(String yearRange) {
        yearRanges.remove(yearRange);
    }

    public void clearYearRanges() {
        yearRanges.clear();
    }

    public void updateAllRanges(List<String> newYearRanges) {
        yearRanges = newYearRanges;
    }

    public Practicioner getPracticioner() {
        return practicioner;
    }

    @Override
    public String toString() {
        return "ClubMember{" +
                "id=" + id +
                ", club=" + club +
                ", practicioner=" + practicioner +
                ", yearRanges=" + yearRanges +
                '}';
    }
}

