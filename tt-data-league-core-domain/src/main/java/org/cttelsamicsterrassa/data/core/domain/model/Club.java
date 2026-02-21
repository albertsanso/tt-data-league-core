package org.cttelsamicsterrassa.data.core.domain.model;

import org.albertsanso.commons.model.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Club extends Entity {
    private final UUID id;
    private String name;
    private List<String> yearRanges = new ArrayList<>();

    public Club(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Club createNew(String name) {
        return new Club(UUID.randomUUID(), name);
    }

    public static Club createExisting(UUID id, String name) {
        return new Club(id, name);
    }

    public void modifyName(String newName) {
        if (!this.name.equals(newName)) this.name = newName;
    }

    public void delete() {
        // Implement deletion logic if needed
    }

    public void addYearRange(String newYearRange) {
        if (!yearRanges.contains(newYearRange)) yearRanges.add(newYearRange);
    }

    public void addYearRanges(List<String> newYearRanges) {
        for (String yearRange : newYearRanges) {
            addYearRange(yearRange);
        }
    }

    public void removeYearRange(String yearRange) {
        yearRanges.remove(yearRange);
    }

    public void clearYearRanges() {
        yearRanges.clear();
    }

    public void setYearRanges(List<String> newYearRanges) {
        yearRanges = newYearRanges;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getYearRanges() {
        return Collections.unmodifiableList(yearRanges);
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearRanges=" + yearRanges +
                '}';
    }

    public static void main(String[] args) {
        Club club = Club.createNew("My Club");
        club.addYearRange("2020-2021");
        club.addYearRange("2021-2022");
        System.out.println(club);

        club.addYearRange("2022-2023");
        System.out.println("After adding a year range: " + club);

        club.addYearRange("2022-2023"); // Adding duplicate
        System.out.println("After adding a duplicate year range: " + club);
    }
}

