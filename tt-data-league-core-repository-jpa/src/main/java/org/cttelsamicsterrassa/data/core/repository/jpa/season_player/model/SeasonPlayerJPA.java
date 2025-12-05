package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.model.ClubMemberJPA;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="SeasonPlayer",
        indexes = {
                @Index(name="idx_club_member_id", columnList="club_member_id")
        }
)
public class SeasonPlayerJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "club_member_id")
    private ClubMemberJPA clubMember;

    @Column(name = "license_tag")
    private String licenseTag;

    @Column(name = "license_ref", length = 100)
    private String licenseRef;

    @Column(name = "season", length = 9)
    private String yearRange;
}
