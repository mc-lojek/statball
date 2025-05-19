package pl.mclojek.statball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.Competition

class CompetitionsViewModel(
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompetitionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCompetitions()
    }

    fun getCompetitions() = viewModelScope.launch {
        val result = ktorClient.getCompetitions()
        _uiState.update { it.copy(competitions = result.competitions) }
    }

    data class CompetitionsUiState(
        val competitions: List<Competition> = emptyList()
    )
}