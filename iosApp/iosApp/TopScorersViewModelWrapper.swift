import Foundation
import shared

class TopScorersViewModelWrapper: ObservableObject {
    private let viewModel: TopScorersViewModel

    @Published var state: ScorersUiState = ScorersUiState()

    init(code: String) {
        
        viewModel = ViewModelFactory().createTopScorersViewModel(code: code)
        
        viewModel.subscribeToUiState { newValue in
            self.state = newValue
        }
    }
}
