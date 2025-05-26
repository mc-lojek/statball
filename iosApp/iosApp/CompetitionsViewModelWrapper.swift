import Foundation
import shared

class CompetitionsViewModelWrapper: ObservableObject {
    private let viewModel = ViewModelFactory().createCompetitionsViewModel()

    @Published var state: CompetitionsUiState = CompetitionsUiState()


    init() {
        viewModel.subscribeToUiState { newValue in
            self.state = newValue
        }
    }

//    func updateValue(_ value: CompetitionsUiState) {
//        viewModel.updateValue(newValue: value)
//    }
}
