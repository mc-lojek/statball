import Foundation
import shared

class TeamMatchesViewModelWrapper: ObservableObject {
    private let viewModel: TeamMatchesViewModel

    @Published var state: TeamMatchesUiState = TeamMatchesUiState()

    init(id: Int32) {
        
        viewModel = ViewModelFactory().createTeamMatchesViewModel(id: id)
        
        viewModel.subscribeToUiState { newValue in
            self.state = newValue
        }
    }
}

struct MatchModel: Identifiable {
    let match: Match

    var id: Int32 { match.id }
    var homeTeam: String { match.homeTeam.name }
    var awayTeam: String { match.awayTeam.name }
}
