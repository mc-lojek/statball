import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import pl.mclojek.statball.*
import pl.mclojek.statball.model.Competition
import pl.mclojek.statball.model.CompetitionsResponse
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class CompetitionsViewModelTest {

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
    fun `getCompetitions updates uiState`() = runTest(testDispatcher) {
        val fakeCompetitions = listOf(
            Competition(id = 1, name = "Premier League", code = "PL", emblem = null)
        )
        val fakeResponse = CompetitionsResponse(competitions = fakeCompetitions)
        val fakeClient = FakeKtorClient(fakeResponse)

        val viewModel = CompetitionsViewModel(fakeClient)

        viewModel.getCompetitions()
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(1, state.competitions.size)
    }

}
