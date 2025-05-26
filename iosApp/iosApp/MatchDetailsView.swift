import SwiftUI
import shared

struct MatchDetailsView: View {
    
    @StateObject var vm: MatchDetailsViewModelWrapper

    init(id: Int32) {
        _vm = StateObject(wrappedValue: MatchDetailsViewModelWrapper(id: id))
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            
            if let matchDetails = vm.state.matchDetails {
                Text("\(matchDetails.homeTeam.name ?? "-") vs \(matchDetails.awayTeam.name)")
                Text("Wynik końcowy: \(formatScore(matchDetails.score.fullTime))")
                    .font(.headline)
                
                Text("Data i godzina meczu: \(formatDate(matchDetails.utcDate))")
                    .font(.subheadline)
                
                Text("Wynik po 1. połowie: \(formatScore(matchDetails.score.halfTime))")
                    .font(.subheadline)
                
                Text("Wynik po 2. połowie: \(formatScore(matchDetails.score.fullTime))")
                    .font(.subheadline)
                
                Spacer()
            } else {
                Text("Match details not available")
            }

        }
        .padding()
        .navigationTitle("Match details")
    }
    
    private func formatScore(_ score: ScoreDetails) -> String {
        let home = score.home != nil ? "\(score.home!)" : "-"
        let away = score.away != nil ? "\(score.away!)" : "-"
        return "\(home) : \(away)"
    }
    
    func formatDate(_ isoDate: String) -> String {
        let isoFormatter = ISO8601DateFormatter()
        if let date = isoFormatter.date(from: isoDate) {
            let formatter = DateFormatter()
            formatter.dateStyle = .medium
            formatter.timeStyle = .short
            return formatter.string(from: date)
        } else {
            return isoDate // fallback na surowy string
        }
    }
}
