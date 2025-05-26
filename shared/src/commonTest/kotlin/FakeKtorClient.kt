package pl.mclojek.statball

import pl.mclojek.statball.model.*

class FakeKtorClient(
    private val competitionsResponse: CompetitionsResponse? = null,
    private val standingsResponse: FootballStandingsResponse? = null,
    private val scorersResponse: FootballScorersResponse? = null,
    private val matchesResponse: FootballMatchesResponse? = null,
    private val matchResponse: FootballMatch? = null,
    private val teamResponse: FootballTeam? = null,
    private val teamMatchesResponse: FootballMatchResponse? = null
) : KtorClient() {

    override suspend fun getCompetitions(): CompetitionsResponse {
        return competitionsResponse ?: CompetitionsResponse(emptyList())
    }

    override suspend fun getStandings(code: String): FootballStandingsResponse {
        return standingsResponse ?: FootballStandingsResponse(
            Competition(
                0,
                "Test Competition",
                "TST",
                null
            ), emptyList()
        )
    }

    override suspend fun getScorers(code: String): FootballScorersResponse {
        return scorersResponse ?: FootballScorersResponse(
            Competition(
                0,
                "Test Competition",
                "TST",
                null
            ), emptyList()
        )
    }

    override suspend fun getMatches(code: String): FootballMatchesResponse {
        return matchesResponse ?: FootballMatchesResponse(
            Competition(
                0,
                "Test Competition",
                "TST",
                null
            ), emptyList()
        )
    }

    override suspend fun getMatch(id: Int): FootballMatch {
        return matchResponse ?: FootballMatch(
            competition = Competition(0, "Test Competition", "TST", null),
            id = id,
            utcDate = "2025-01-01T00:00:00Z",
            status = "SCHEDULED",
            matchday = 1,
            homeTeam = Team(1, "Home Team", "HOME", "HT", crest = ""),
            awayTeam = Team(2, "Away Team", "AWAY", "AT", crest = ""),
            venue = null,
            lastUpdated = "",
            referees = emptyList(),
            score = Score(null, "", ScoreDetails(12, 12), ScoreDetails(2, 3)),
        )
    }

    override suspend fun getTeam(id: Int): FootballTeam {
        return teamResponse ?: FootballTeam(
            id = id,
            name = "Test Team",
            shortName = "TT",
            tla = "TT",
            crest = "",
            clubColors = "Red/White",
            venue = "Test Arena",
            coach = Coach(123, "Test", "Testowy", "Test Testowy", "12.02.2000", "PL"),
            squad = emptyList()
        )
    }

    override suspend fun getTeamMatches(id: Int): FootballMatchResponse {
        return teamMatchesResponse ?: FootballMatchResponse(
            ResultSet(1, "", 1, 1, 1, 1),
            emptyList()
        )
    }
}
