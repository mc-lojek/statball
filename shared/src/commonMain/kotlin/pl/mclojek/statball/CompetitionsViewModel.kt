package pl.mclojek.statball

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mclojek.statball.model.Competition
import subscribe

class CompetitionsViewModel(
    private val ktorClient: KtorClient
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CompetitionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCompetitions()
    }

    fun getCompetitions() = viewModelScope.launch {
        try {
            val result = ktorClient.getCompetitions()
            _uiState.update { it.copy(competitions = result.competitions) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun subscribeToUiState(onEach: (CompetitionsUiState) -> Unit) {
        uiState.subscribe(onEach = onEach)
    }
}

data class CompetitionsUiState(
    val competitions: List<Competition> = emptyList()
) {
    constructor() : this(emptyList())
}