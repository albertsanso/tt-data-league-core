package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="Practicioner",
        indexes = {
                @Index(name="idx_full_name", columnList="full_name")
        }
)
public class PracticionerJPA {
    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="second_name")
    private String secondName;

    @NotNull
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_date")
    private Date birthDate;
}
