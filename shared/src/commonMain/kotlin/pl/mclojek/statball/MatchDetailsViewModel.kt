package pl.mclojek.statball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.FootballMatch
import subscribe

class MatchDetailsViewModel(
    private val matchId: Int,
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMatchDetails(matchId)
    }

    fun getMatchDetails(matchId: Int) = viewModelScope.launch {
        val result = ktorClient.getMatch(matchId)
        _uiState.update { it.copy(matchDetails = result) }
    }

    fun subscribeToUiState(onEach: (MatchDetailsUiState) -> Unit) {
        uiState.subscribe(onEach = onEach)
    }
}

data class MatchDetailsUiState(
    val matchDetails: FootballMatch? = null
) {
    constructor() : this(null)
}