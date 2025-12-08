package org.cttelsamicsterrassa.data.core.repository.jpa.match.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerResultJPA;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="PlayersSingleMatch",
        indexes = {
                @Index(name="idx_player_result_abc_id", columnList="player_result_abc_id"),
                @Index(name="idx_player_result_xyz_id", columnList="player_result_xyz_id"),
                @Index(name="idx_unique_row_match_id", columnList="unique_row_match_id")
        }
)
public class PlayersSingleMatchJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "player_result_abc_id")
    private SeasonPlayerResultJPA seasonPlayerResultAbc;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "player_result_xyz_id")
    private SeasonPlayerResultJPA seasonPlayerResultXyz;

    @Column(name = "season", length = 9)
    private String season;

    @Column(name = "competition_type")
    private String competitionType;

    @Column(name = "competition_category")
    private String competitionCategory;

    @Column(name = "competition_scope")
    private String competitionScope;

    @Column(name = "competition_scopetag")
    private String competitionScopeTag;

    @Column(name = "competition_group")
    private String competitionGroup;

    @Column(name = "competition_gender")
    private String competitionGender;

    @Column(name ="match_day_number")
    private int matchDayNumber;

    @Column(name="unique_row_match_id", unique = false)
    private String uniqueRowMatchId;

}
