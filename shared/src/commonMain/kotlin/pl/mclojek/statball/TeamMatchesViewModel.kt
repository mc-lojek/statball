package pl.mclojek.statball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.FootballMatchResponse

class TeamMatchesViewModel(
    private val teamId: Int,
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamMatchesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTeamMatches(teamId)
    }

    fun getTeamMatches(teamId: Int) = viewModelScope.launch {
        val result = ktorClient.getTeamMatches(teamId)
        _uiState.update { it.copy(teamMatches = result) }
    }

    data class TeamMatchesUiState(
        val teamMatches: FootballMatchResponse? = null,
    )
}