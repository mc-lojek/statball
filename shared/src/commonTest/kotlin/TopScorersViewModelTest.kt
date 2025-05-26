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
class TopScorersViewModelTest {

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
    fun `getScorers updates uiState`() = runTest(testDispatcher) {
        val fakeScorers = listOf(
            Scorer(
                player = Player(id = 1, name = "John", position = "Forward", nationality = "PL", dateOfBirth = "1990-01-01"),
                team = Team(id = 1, name = "Test Team", shortName = "TT", tla = "TT", crest = ""),
                goals = 10,
                assists = 5,
                penalties = 2
            )
        )
        val fakeResponse = FootballScorersResponse(
            competition = Competition(id = 1, name = "Premier League", code = "PL", emblem = null),
            scorers = fakeScorers
        )
        val fakeClient = FakeKtorClient(scorersResponse = fakeResponse)

        val viewModel = TopScorersViewModel("PL", fakeClient)

        // getScorers is called in init, so just advance time
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(1, state.scorers.size)
        assertEquals("John", state.scorers.first().player.name)
    }
}