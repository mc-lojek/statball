import Foundation
import shared

class TeamDetailsViewModelWrapper: ObservableObject {
    private let viewModel: TeamDetailsViewModel

    @Published var state: TeamDetailsUiState = TeamDetailsUiState()

    init(id: Int32) {
        
        viewModel = ViewModelFactory().createTeamDetailsViewModel(id: id)
        
        viewModel.subscribeToUiState { newValue in
            self.state = newValue
        }
    }
}
