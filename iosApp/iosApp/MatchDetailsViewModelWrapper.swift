import Foundation
import shared

class MatchDetailsViewModelWrapper: ObservableObject {
    private let viewModel: MatchDetailsViewModel

    @Published var state: MatchDetailsUiState = MatchDetailsUiState()

    init(id: Int32) {
        
        viewModel = ViewModelFactory().createMatchDetailsViewModel(id: id)
        
        viewModel.subscribeToUiState { newValue in
            self.state = newValue
        }
    }
}
