package org.cttelsamicsterrassa.data.core.domain.service;

import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.domain.repository.PlayersSingleMatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchQueryService {

    private static final String INVALID_PRACTICIONER_NAME_MESSAGE = "Practicioner name fragment cannot be null or blank";

    private final PlayersSingleMatchRepository playersSingleMatchRepository;

    public MatchQueryService(PlayersSingleMatchRepository playersSingleMatchRepository) {
        this.playersSingleMatchRepository = playersSingleMatchRepository;
    }

    public List<PlayersSingleMatch> findMatchesByPracticionerName(String practicionerName, String season, CompetitionInfo competitionInfo, Integer matchDayNumber) {
        if (practicionerName == null || practicionerName.isBlank()) {
            throw new IllegalArgumentException(INVALID_PRACTICIONER_NAME_MESSAGE);
        }

        return playersSingleMatchRepository.findBySeasonAndCompetitionAndMatchDayNumber(
                season,
                competitionInfo,
                matchDayNumber,
                practicionerName
        );
    }
}

