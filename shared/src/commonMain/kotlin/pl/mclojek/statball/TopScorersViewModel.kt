package pl.mclojek.statball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.Scorer

class TopScorersViewModel(
    private val code: String,
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScorersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        this.getScorers(code)
    }

    fun getScorers(code: String) = viewModelScope.launch {
        val result = ktorClient.getScorers(code)
        _uiState.update { it.copy(scorers = result.scorers) }
    }

    data class ScorersUiState(
        val scorers: List<Scorer> = emptyList()
    )
}