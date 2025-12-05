package org.cttelsamicsterrassa.data.core.domain.model;

import org.albertsanso.commons.model.Entity;

import java.util.Date;
import java.util.UUID;

public class Practicioner extends Entity {
    private final UUID id;
    private String firstName;
    private String secondName;
    private String fullName;
    private Date birthDate;

    private Practicioner(UUID id, String firstName, String secondName, String fullName, Date birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public static Practicioner createNew(String firstName, String secondName, String fullName, Date birthDate) {
        return new Practicioner(
                UUID.randomUUID(),
                firstName,
                secondName,
                fullName,
                birthDate
        );
    }

    public static Practicioner createExisting(UUID id, String firstName, String secondName, String fullName, Date birthDate) {
        return new Practicioner(
                id,
                firstName,
                secondName,
                fullName,
                birthDate
        );
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Practicioner{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
