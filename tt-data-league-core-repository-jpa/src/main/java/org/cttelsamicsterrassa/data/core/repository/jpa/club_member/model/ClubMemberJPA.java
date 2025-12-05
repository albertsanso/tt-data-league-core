package org.cttelsamicsterrassa.data.core.repository.jpa.club_member.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.model.ClubJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="ClubMember",
        indexes = {
                @Index(name="idx_club_id", columnList="club_id"),
                @Index(name="idx_practicioner_id", columnList="practicioner_id")
        }
)
public class ClubMemberJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "club_id")
    private ClubJPA club;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "practicioner_id")
    private PracticionerJPA practicioner;

    @ElementCollection
    @CollectionTable(
            name = "ClubMemberYearRange",
            schema = "bcnesadata",
            joinColumns = @JoinColumn(name = "club_member_id")
    )
    @Column(name = "year_range")
    @OrderColumn(name = "order_index")
    private List<String> yearRanges = new ArrayList<>();
}
