package pl.mclojek.statball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.Match

class CompetitionMatchesViewModel(
    private val code: String,
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        this.getMatches(code)
    }

    fun getMatches(code: String) = viewModelScope.launch {
        val result = ktorClient.getMatches(code)
        _uiState.update { it.copy(matches = result.matches) }
    }

    data class MatchesUiState(
        val matches: List<Match> = emptyList()
    )
}