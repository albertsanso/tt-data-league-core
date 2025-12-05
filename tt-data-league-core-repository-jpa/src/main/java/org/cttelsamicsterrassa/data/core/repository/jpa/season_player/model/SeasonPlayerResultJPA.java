package org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import org.cttelsamicsterrassa.data.core.repository.shared.StringListConverter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="SeasonPlayerResult",
        indexes = {
                @Index(name="idx_season_player_id", columnList = "season_player_id")
        }
)
public class SeasonPlayerResultJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

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

    @Column(name ="match_day_number")
    private int matchDayNumber;

    @Column(name = "match_day", length = 20)
    private String matchDay;

    @Column(name = "match_player_letter")
    private String matchPlayerLetter;

    @Convert(converter = StringListConverter.class)
    @Column(name = "match_game_points", length = 500)
    private List<String> matchGamePoints;

    @Column(name = "match_games_won")
    private int matchGamesWon;

    @Column(name = "match_linkage_id")
    private String matchLinkageId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "season_player_id")
    private SeasonPlayerJPA seasonPlayer;
}
