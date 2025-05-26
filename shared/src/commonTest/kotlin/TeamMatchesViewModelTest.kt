package pl.mclojek.statball

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pl.mclojek.statball.model.*

@OptIn(ExperimentalCoroutinesApi::class)
class TeamMatchesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTeamMatches updates uiState`() = runTest(testDispatcher) {
        val fakeMatch = FootballMatch(
            competition = Competition(1, "Premier League", "PL", null),
            id = 100,
            utcDate = "2025-01-01T12:00:00Z",
            status = "SCHEDULED",
            matchday = 1,
            homeTeam = Team(1, "Home", "HOME", "HM", crest = ""),
            awayTeam = Team(2, "Away", "AWAY", "AW", crest = ""),
            venue = "Stadium",
            lastUpdated = "",
            referees = emptyList(),
            score = Score(null, "", ScoreDetails(0, 0), ScoreDetails(0, 0)),
        )
        val fakeResponse = FootballMatchResponse(
            resultSet = ResultSet(1, "2025-01-01", 1, 1, 1, 1),
            matches = listOf(fakeMatch)
        )
        val fakeClient = FakeKtorClient(teamMatchesResponse = fakeResponse)

        val viewModel = TeamMatchesViewModel(teamId = 1, ktorClient = fakeClient)

        // getTeamMatches is called in init, so just advance time
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(1, state.teamMatches?.matches?.size)
        assertEquals(100, state.teamMatches?.matches?.first()?.id)
    }
}