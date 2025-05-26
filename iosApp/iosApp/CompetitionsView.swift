import SwiftUI
import shared

struct CompetitionsView: View {
    
    @StateObject var vmWrapper = CompetitionsViewModelWrapper()

    var body: some View {
        NavigationStack {
            VStack {
                // Centered title at the top
                Text("Competitions")
                    .font(.title)
                    .bold()
                    .padding()

                // Your list of competitions
                List(vmWrapper.state.competitions, id: \.id) { competition in
                    NavigationLink(destination: CompetitionMatchesView(code: competition.code ?? "")) {
                        Text(competition.name ?? "no-name")
                    }
                }
            }
            .navigationTitle("") // optional: removes default navigation bar title
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
