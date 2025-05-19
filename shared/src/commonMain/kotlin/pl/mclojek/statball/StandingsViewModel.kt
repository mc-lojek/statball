package pl.mclojek.statball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.TeamStanding

class StandingsViewModel(
    private val code: String,
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(StandingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getStandings(code)
    }

    fun getStandings(code: String) = viewModelScope.launch {
        val result = ktorClient.getStandings(code)
        _uiState.update { it.copy(standings = result.standings.flatMap { it.table }) }
    }

    data class StandingsUiState(
        val standings: List<TeamStanding> = emptyList()
    )
}