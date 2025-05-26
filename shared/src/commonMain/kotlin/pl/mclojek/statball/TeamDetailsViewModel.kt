package pl.mclojek.statball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.FootballTeam
import pl.mclojek.statball.model.Team
import pl.mclojek.statball.model.TeamStanding
import subscribe

class TeamDetailsViewModel(
    private val teamId: Int,
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTeamDetails(teamId)
    }

    fun getTeamDetails(teamId: Int) = viewModelScope.launch {
        val result = ktorClient.getTeam(teamId)
        _uiState.update { it.copy(teamDetails = result) }
    }

    fun subscribeToUiState(onEach: (TeamDetailsUiState) -> Unit) {
        uiState.subscribe(onEach = onEach)
    }
}

data class TeamDetailsUiState(
    val teamDetails: FootballTeam? = null,
) {
    constructor() : this(null)
}