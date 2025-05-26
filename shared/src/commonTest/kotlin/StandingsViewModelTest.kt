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
class StandingsViewModelTest {

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
    fun `getStandings updates uiState with standings`() = runTest(testDispatcher) {
        val fakeTable = listOf(
            TeamStanding(
                position = 1,
                team = Team(1, "Team A", "TA", "TA", crest = ""),
                playedGames = 10,
                form = null,
                won = 7,
                draw = 2,
                lost = 1,
                points = 23,
                goalsFor = 20,
                goalsAgainst = 10,
                goalDifference = 10
            )
        )
        val fakeStandings = listOf(
            Standing(
                stage = "REGULAR_SEASON",
                type = "TOTAL",
                group = null,
                table = fakeTable
            )
        )
        val fakeResponse = FootballStandingsResponse(
            competition = Competition(1, "Premier League", "PL", null),
            standings = fakeStandings
        )
        val fakeClient = FakeKtorClient(standingsResponse = fakeResponse)

        val viewModel = StandingsViewModel("PL", fakeClient)

        // Wait for init block to finish
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(1, state.standings.size)
        assertEquals("Team A", state.standings.first().team.name)
    }
}