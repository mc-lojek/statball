import Foundation
import shared

class CompetitionMatchesViewModelWrapper: ObservableObject {
    private let viewModel: CompetitionMatchesViewModel

    @Published var state: MatchesUiState = MatchesUiState()


    init(code: String) {
        
        viewModel = ViewModelFactory().createCompetitionMatchesViewModel(code: code)
        
        viewModel.subscribeToUiState { newValue in
            self.state = newValue
        }
    }
}
