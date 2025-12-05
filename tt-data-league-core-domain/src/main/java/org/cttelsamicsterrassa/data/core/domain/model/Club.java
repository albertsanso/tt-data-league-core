package org.cttelsamicsterrassa.data.core.domain.model;

import org.albertsanso.commons.model.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Club extends Entity {
    private final UUID id;
    private final String name;
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

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getYearRanges() {
        return Collections.unmodifiableList(yearRanges);
    }

    public void addYearRange(String newYearRange) {
        yearRanges.add(newYearRange);
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

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearRanges=" + yearRanges +
                '}';
    }
}

