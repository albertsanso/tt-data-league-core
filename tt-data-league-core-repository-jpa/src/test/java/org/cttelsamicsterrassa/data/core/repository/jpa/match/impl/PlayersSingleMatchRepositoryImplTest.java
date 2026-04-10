package org.cttelsamicsterrassa.data.core.repository.jpa.match.impl;

import org.cttelsamicsterrassa.data.core.domain.model.CompetitionInfo;
import org.cttelsamicsterrassa.data.core.domain.model.PlayersSingleMatch;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.impl.ClubRepositoryHelper;
import org.cttelsamicsterrassa.data.core.repository.jpa.club.model.ClubJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.impl.ClubMemberRepositoryHelper;
import org.cttelsamicsterrassa.data.core.repository.jpa.club_member.model.ClubMemberJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchJPAToPlayersSingleMatchMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.mapper.PlayersSingleMatchToPlayersSingleMatchJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.match.model.PlayersSingleMatchJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.impl.PracticionerRepositoryHelper;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl.SeasonPlayerRepositoryHelper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.impl.SeasonPlayerResultRepositoryHelper;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.season_player.model.SeasonPlayerResultJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PlayersSingleMatchRepositoryImplTest.TestApplication.class)
@Import(PlayersSingleMatchRepositoryImpl.class)
class PlayersSingleMatchRepositoryImplTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
    @EnableJpaRepositories(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
    static class TestApplication {
    }

    @Autowired
    private PlayersSingleMatchRepositoryImpl repository;

    @Autowired
    private ClubRepositoryHelper clubRepositoryHelper;

    @Autowired
    private PracticionerRepositoryHelper practicionerRepositoryHelper;

    @Autowired
    private ClubMemberRepositoryHelper clubMemberRepositoryHelper;

    @Autowired
    private SeasonPlayerRepositoryHelper seasonPlayerRepositoryHelper;

    @Autowired
    private SeasonPlayerResultRepositoryHelper seasonPlayerResultRepositoryHelper;

    @Autowired
    private PlayersSingleMatchRepositoryHelper playersSingleMatchRepositoryHelper;

    @MockitoBean
    private PlayersSingleMatchJPAToPlayersSingleMatchMapper fromJpaMapper;

    @MockitoBean
    private PlayersSingleMatchToPlayersSingleMatchJPAMapper toJpaMapper;

    @BeforeEach
    void setUp() {
        when(fromJpaMapper.apply(any(PlayersSingleMatchJPA.class)))
                .thenAnswer(invocation -> mock(PlayersSingleMatch.class));
    }

    @Test
    void findBySeasonAndCompetitionAndMatchDayNumberMatchesCaseInsensitiveAndPartialName() {
        CompetitionInfo competitionInfo = new CompetitionInfo("LEAGUE", "SENIOR", "LOCAL", "Q1", "GROUP_1", "MIXED");

        persistPlayersSingleMatch("John Smith", "Jane Doe", "2025-2026", competitionInfo, 4, "A");
        persistPlayersSingleMatch("Alice Brown", "Robert Black", "2025-2026", competitionInfo, 4, "B");

        List<PlayersSingleMatch> result = repository.findBySeasonAndCompetitionAndMatchDayNumber(
                "2025-2026",
                competitionInfo,
                4,
                "sMi"
        );

        assertEquals(1, result.size());
    }

    @Test
    void findBySeasonAndCompetitionAndMatchDayNumberAppliesCombinedFilters() {
        CompetitionInfo competitionInfo = new CompetitionInfo("LEAGUE", "SENIOR", "LOCAL", "Q2", "GROUP_1", "MIXED");

        persistPlayersSingleMatch("John Smith", "Jane Doe", "2025-2026", competitionInfo, 4, "A");
        persistPlayersSingleMatch("John Smith", "Jane Doe", "2025-2026", competitionInfo, 5, "B");
        persistPlayersSingleMatch("John Smith", "Jane Doe", "2024-2025", competitionInfo, 4, "C");

        List<PlayersSingleMatch> result = repository.findBySeasonAndCompetitionAndMatchDayNumber(
                "2025-2026",
                competitionInfo,
                4,
                "smith"
        );

        assertEquals(1, result.size());
    }

    @Test
    void findBySeasonAndCompetitionAndMatchDayNumberReturnsEmptyWhenNoMatches() {
        CompetitionInfo competitionInfo = new CompetitionInfo("LEAGUE", "SENIOR", "LOCAL", "Q3", "GROUP_1", "MIXED");

        persistPlayersSingleMatch("John Smith", "Jane Doe", "2025-2026", competitionInfo, 4, "A");

        List<PlayersSingleMatch> result = repository.findBySeasonAndCompetitionAndMatchDayNumber(
                "2025-2026",
                competitionInfo,
                4,
                "non-existent-name"
        );

        assertEquals(0, result.size());
    }

    private void persistPlayersSingleMatch(
            String localPracticionerName,
            String visitorPracticionerName,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String suffix) {

        ClubJPA club = new ClubJPA();
        club.setId(UUID.randomUUID());
        club.setName("Club-" + suffix + "-" + UUID.randomUUID());
        clubRepositoryHelper.save(club);

        PracticionerJPA localPracticioner = new PracticionerJPA();
        localPracticioner.setId(UUID.randomUUID());
        localPracticioner.setFullName(localPracticionerName);
        localPracticioner.setFirstName(localPracticionerName);
        localPracticioner.setSecondName("");
        localPracticioner.setBirthDate(new Date());
        practicionerRepositoryHelper.save(localPracticioner);

        PracticionerJPA visitorPracticioner = new PracticionerJPA();
        visitorPracticioner.setId(UUID.randomUUID());
        visitorPracticioner.setFullName(visitorPracticionerName);
        visitorPracticioner.setFirstName(visitorPracticionerName);
        visitorPracticioner.setSecondName("");
        visitorPracticioner.setBirthDate(new Date());
        practicionerRepositoryHelper.save(visitorPracticioner);

        ClubMemberJPA localClubMember = new ClubMemberJPA();
        localClubMember.setId(UUID.randomUUID());
        localClubMember.setClub(club);
        localClubMember.setPracticioner(localPracticioner);
        clubMemberRepositoryHelper.save(localClubMember);

        ClubMemberJPA visitorClubMember = new ClubMemberJPA();
        visitorClubMember.setId(UUID.randomUUID());
        visitorClubMember.setClub(club);
        visitorClubMember.setPracticioner(visitorPracticioner);
        clubMemberRepositoryHelper.save(visitorClubMember);

        SeasonPlayerJPA localSeasonPlayer = new SeasonPlayerJPA();
        localSeasonPlayer.setId(UUID.randomUUID());
        localSeasonPlayer.setClubMember(localClubMember);
        localSeasonPlayer.setYearRange(season);
        localSeasonPlayer.setLicenseTag("L");
        localSeasonPlayer.setLicenseRef("L-" + suffix + "-1");
        seasonPlayerRepositoryHelper.save(localSeasonPlayer);

        SeasonPlayerJPA visitorSeasonPlayer = new SeasonPlayerJPA();
        visitorSeasonPlayer.setId(UUID.randomUUID());
        visitorSeasonPlayer.setClubMember(visitorClubMember);
        visitorSeasonPlayer.setYearRange(season);
        visitorSeasonPlayer.setLicenseTag("V");
        visitorSeasonPlayer.setLicenseRef("V-" + suffix + "-1");
        seasonPlayerRepositoryHelper.save(visitorSeasonPlayer);

        SeasonPlayerResultJPA localResult = new SeasonPlayerResultJPA();
        localResult.setId(UUID.randomUUID());
        fillResultFields(localResult, season, competitionInfo, matchDayNumber, "A", localSeasonPlayer);
        seasonPlayerResultRepositoryHelper.save(localResult);

        SeasonPlayerResultJPA visitorResult = new SeasonPlayerResultJPA();
        visitorResult.setId(UUID.randomUUID());
        fillResultFields(visitorResult, season, competitionInfo, matchDayNumber, "X", visitorSeasonPlayer);
        seasonPlayerResultRepositoryHelper.save(visitorResult);

        PlayersSingleMatchJPA match = new PlayersSingleMatchJPA();
        match.setId(UUID.randomUUID());
        match.setSeasonPlayerResultLocal(localResult);
        match.setSeasonPlayerResultVisitor(visitorResult);
        match.setSeason(season);
        match.setCompetitionType(competitionInfo.competitionType());
        match.setCompetitionCategory(competitionInfo.competitionCategory());
        match.setCompetitionScope(competitionInfo.competitionScope());
        match.setCompetitionScopeTag(competitionInfo.competitionScopeTag());
        match.setCompetitionGroup(competitionInfo.competitionGroup());
        match.setCompetitionGender(competitionInfo.competitionGender());
        match.setMatchDayNumber(matchDayNumber);
        match.setUniqueRowMatchId("URM-" + suffix + "-" + UUID.randomUUID());
        match.setMatchDate(ZonedDateTime.now());

        playersSingleMatchRepositoryHelper.saveAndFlush(match);
    }

    private void fillResultFields(
            SeasonPlayerResultJPA result,
            String season,
            CompetitionInfo competitionInfo,
            int matchDayNumber,
            String playerLetter,
            SeasonPlayerJPA seasonPlayer) {
        result.setSeason(season);
        result.setCompetitionType(competitionInfo.competitionType());
        result.setCompetitionCategory(competitionInfo.competitionCategory());
        result.setCompetitionScope(competitionInfo.competitionScope());
        result.setCompetitionScopeTag(competitionInfo.competitionScopeTag());
        result.setCompetitionGroup(competitionInfo.competitionGroup());
        result.setCompetitionGender(competitionInfo.competitionGender());
        result.setMatchDayNumber(matchDayNumber);
        result.setMatchDay("Day " + matchDayNumber);
        result.setMatchPlayerLetter(playerLetter);
        result.setMatchGamePoints(List.of("11-8", "11-9", "11-7"));
        result.setMatchGamesWon(3);
        result.setPlayersPairing("P1-P2");
        result.setTeamRole("PLAYER");
        result.setSeasonPlayer(seasonPlayer);
    }
}



