// kotlin
package pl.mclojek.statball

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import pl.mclojek.statball.model.Competition
import pl.mclojek.statball.model.CompetitionsResponse

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
    fun `getCompetitions updates uiState with competitions`() = runTest(testDispatcher) {
        val fakeCompetitions = listOf(
            Competition(id = 1, name = "Premier League", code = "PL", emblem = null),
            Competition(id = 2, name = "La Liga", code = "LL", emblem = null)
        )
        val fakeResponse = CompetitionsResponse(competitions = fakeCompetitions)
        val fakeClient = FakeKtorClient(competitionsResponse = fakeResponse)

        val viewModel = CompetitionsViewModel(fakeClient)

        viewModel.getCompetitions()
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(2, state.competitions.size)
        assertEquals("Premier League", state.competitions[0].name)
        assertEquals("La Liga", state.competitions[1].name)
    }

    @Test
    fun `getCompetitions handles exception and keeps competitions empty`() = runTest(testDispatcher) {
        val errorClient = object : KtorClient() {
            override suspend fun getCompetitions(): CompetitionsResponse {
                throw RuntimeException("Network error")
            }
        }

        val viewModel = CompetitionsViewModel(errorClient)

        viewModel.getCompetitions()
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertTrue(state.competitions.isEmpty())
    }
}