package pl.mclojek.statball

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pl.mclojek.statball.model.Coach
import pl.mclojek.statball.model.FootballTeam

@OptIn(ExperimentalCoroutinesApi::class)
class TeamDetailsViewModelTest {

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
    fun `getTeamDetails updates uiState with team details`() = runTest(testDispatcher) {
        val fakeTeam = FootballTeam(
            id = 42,
            name = "Test Team",
            shortName = "TT",
            tla = "TT",
            crest = "crest.png",
            clubColors = "Red/White",
            venue = "Test Arena",
            coach = Coach(1, "Jan", "Kowalski", "Jan Kowalski", "1990-01-01", "PL"),
            squad = emptyList()
        )
        val fakeClient = FakeKtorClient(teamResponse = fakeTeam)
        val viewModel = TeamDetailsViewModel(teamId = 42, ktorClient = fakeClient)

        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(fakeTeam, state.teamDetails)
    }
}