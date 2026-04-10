package org.cttelsamicsterrassa.data.core.domain.service;

import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.domain.repository.PlayersSingleMatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchQueryServiceTest {

    @Mock
    private PlayersSingleMatchRepository playersSingleMatchRepository;

    private MatchQueryService matchQueryService;

    @BeforeEach
    void setUp() {
        matchQueryService = new MatchQueryService(playersSingleMatchRepository);
    }

    @Test
    void findMatchesByPracticionerNameReturnsRepositoryResultsForValidInput() {
        CompetitionInfo competitionInfo = new CompetitionInfo("LEAGUE", "SENIOR", "LOCAL", "A", "GROUP_1", "MIXED");
        List<PlayersSingleMatch> expectedMatches = List.of(org.mockito.Mockito.mock(PlayersSingleMatch.class));

        when(playersSingleMatchRepository.findBySeasonAndCompetitionAndMatchDayNumber("2025-2026", competitionInfo, 4, "smith"))
                .thenReturn(expectedMatches);

        List<PlayersSingleMatch> actualMatches = matchQueryService.findMatchesByPracticionerName("smith", "2025-2026", competitionInfo, 4);

        assertIterableEquals(expectedMatches, actualMatches);
        verify(playersSingleMatchRepository).findBySeasonAndCompetitionAndMatchDayNumber("2025-2026", competitionInfo, 4, "smith");
    }

    @Test
    void findMatchesByPracticionerNameReturnsEmptyListWhenRepositoryHasNoMatches() {
        CompetitionInfo competitionInfo = new CompetitionInfo("LEAGUE", "SENIOR", "LOCAL", "A", "GROUP_1", "MIXED");
        when(playersSingleMatchRepository.findBySeasonAndCompetitionAndMatchDayNumber("2025-2026", competitionInfo, 4, "unknown"))
                .thenReturn(List.of());

        List<PlayersSingleMatch> result = matchQueryService.findMatchesByPracticionerName("unknown", "2025-2026", competitionInfo, 4);

        assertEquals(0, result.size());
        verify(playersSingleMatchRepository).findBySeasonAndCompetitionAndMatchDayNumber("2025-2026", competitionInfo, 4, "unknown");
    }

    @Test
    void findMatchesByPracticionerNameThrowsForNullPracticionerName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matchQueryService.findMatchesByPracticionerName(null, "2025-2026", null, 4)
        );

        assertEquals("Practicioner name fragment cannot be null or blank", exception.getMessage());
        verifyNoInteractions(playersSingleMatchRepository);
    }

    @Test
    void findMatchesByPracticionerNameThrowsForBlankPracticionerName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matchQueryService.findMatchesByPracticionerName("   ", "2025-2026", null, 4)
        );

        assertEquals("Practicioner name fragment cannot be null or blank", exception.getMessage());
        verifyNoInteractions(playersSingleMatchRepository);
    }
}

