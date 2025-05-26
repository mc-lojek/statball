import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pl.mclojek.statball.*
import pl.mclojek.statball.model.Competition
import pl.mclojek.statball.model.FootballMatch

@OptIn(ExperimentalCoroutinesApi::class)
class MatchDetailsViewModelTest {

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
    fun `getMatchDetails updates uiState with match details`() = runTest(testDispatcher) {
        val fakeMatch = FootballMatch(
            competition = Competition(1, "Premier League", "PL", null),
            id = 42,
            utcDate = "2025-01-01T00:00:00Z",
            status = "SCHEDULED",
            matchday = 1,
            homeTeam = pl.mclojek.statball.model.Team(1, "Home", "HOME", "H", crest = ""),
            awayTeam = pl.mclojek.statball.model.Team(2, "Away", "AWAY", "A", crest = ""),
            venue = "Test Venue",
            lastUpdated = "",
            referees = emptyList(),
            score = pl.mclojek.statball.model.Score(null, "", pl.mclojek.statball.model.ScoreDetails(0, 0), pl.mclojek.statball.model.ScoreDetails(0, 0)),
        )
        val fakeClient = FakeKtorClient(matchResponse = fakeMatch)
        val viewModel = MatchDetailsViewModel(matchId = 42, ktorClient = fakeClient)

        // Wait for init block to finish
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(fakeMatch, state.matchDetails)
    }
}