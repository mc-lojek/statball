import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import pl.mclojek.statball.*
import pl.mclojek.statball.model.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class CompetitionMatchesViewModelTest {

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
    fun `getMatches updates uiState`() = runTest(testDispatcher) {
        val fakeMatches = listOf(
            Match(
                id = 1,
                utcDate = "2025-01-01T00:00:00Z",
                status = "SCHEDULED",
                matchday = 1,
                homeTeam = Team(1, "Home Team", "HOME", "HT", crest = ""),
                awayTeam = Team(2, "Away Team", "AWAY", "AT", crest = ""),
                score = Score(null, "", ScoreDetails(0, 0), ScoreDetails(0, 0)),
                referees = emptyList(),
                venue = null,
                competition = Competition(0, "Test Competition", "TST", null),
                lastUpdated = ""
            )
        )
        val fakeResponse = FootballMatchesResponse(
            competition = Competition(0, "Test Competition", "TST", null),
            matches = fakeMatches
        )
        val fakeClient = FakeKtorClient(matchesResponse = fakeResponse)

        val viewModel = CompetitionMatchesViewModel("TST", fakeClient)

        // getMatches is called in init, so just advance
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(1, state.matches.size)
        assertEquals("Home Team", state.matches.first().homeTeam.name)
    }
}