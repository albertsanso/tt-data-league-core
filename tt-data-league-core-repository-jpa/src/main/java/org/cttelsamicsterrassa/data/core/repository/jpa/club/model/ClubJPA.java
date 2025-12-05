package org.cttelsamicsterrassa.data.core.repository.jpa.club.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="Club",
        indexes = {
                @Index(name="idx_name", columnList="name", unique=true)
        }
)
public class ClubJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @ElementCollection
    @CollectionTable(
            name = "ClubYearRange",
            schema = "bcnesadata",
            joinColumns = @JoinColumn(name = "club_id")
    )
    @Column(name = "year_range")
    @OrderColumn(name = "order_index")
    private List<String> yearRanges = new ArrayList<>();
}
