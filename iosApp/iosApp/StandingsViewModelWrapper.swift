import Foundation
import shared

class StandingsViewModelWrapper: ObservableObject {
    private let viewModel: StandingsViewModel

    @Published var state: StandingsUiState = StandingsUiState()

    init(code: String) {
        
        viewModel = ViewModelFactory().createStandingsViewModel(code: code)
        
        viewModel.subscribeToUiState { newValue in
            self.state = newValue
        }
    }
}


struct PlayerModel: Identifiable {
    let player: Player

    var id: Int32 { player.id }
    var name: String { player.name }
    var shirtNumber: Int? { player.shirtNumber?.intValue } // if applicable
    var section: String {player.section ?? "-"}
}

extension KotlinInt {
    open override var intValue: Int { Int(truncating: self) }
}
