import SwiftUI
import shared

struct CompetitionMatchesView: View {
    let code: String
    @State private var selectedTab = 0

    var body: some View {
        VStack {
            HStack {
                ForEach(0..<3) { index in
                    Button(action: { selectedTab = index }) {
                        Text(tabTitle(for: index))
                            .foregroundColor(selectedTab == index ? .blue : .gray)
                            .fontWeight(selectedTab == index ? .bold : .regular)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 8)
                    }
                }
            }
            .background(Color(UIColor.systemGray6))
            .cornerRadius(8)
            .padding(.horizontal)

            TabView(selection: $selectedTab) {
                MatchesTab(code: code).tag(0)
                StandingsTab(code: code).tag(1)
                TopScorersTab(code: code).tag(2)
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
        }
        .navigationTitle("\(code)")
    }

    private func tabTitle(for index: Int) -> String {
        switch index {
        case 0: return "Matches"
        case 1: return "Standings"
        case 2: return "Top scorers"
        default: return "Tab"
        }
    }
}

struct MatchesTab: View {
    @StateObject var vm: CompetitionMatchesViewModelWrapper

    init(code: String) {
        _vm = StateObject(wrappedValue: CompetitionMatchesViewModelWrapper(code: code))
    }

    var body: some View {
        List(vm.state.matches, id: \.id) { match in
            NavigationLink(destination: MatchDetailsView(id: match.id)) {
                VStack(alignment: .leading) {
                    Text("\(match.homeTeam.name) vs \(match.awayTeam.name)")
                    Text("Status: \(match.status ?? "unknown")").font(.subheadline).foregroundColor(
                        (match.status == "FINISHED") ? .red :
                        (match.status == "SCHEDULED") ? .green :
                        (match.status == "LIVE") ? .yellow :
                        .gray
                    )
                    Text("\(formatDate(match.utcDate))").font(.subheadline).foregroundColor(.gray)
                }
            }
        }
    }
}

struct StandingsTab: View {
    @StateObject var vm: StandingsViewModelWrapper

    init(code: String) {
        _vm = StateObject(wrappedValue: StandingsViewModelWrapper(code: code))
    }

    var body: some View {
            List(vm.state.standings, id: \.team.id) { standing in
                VStack(alignment: .leading) {
                    NavigationLink(destination: TeamDetailsView(id: standing.team.id)) {
                        Text("\(standing.position). \(standing.team.name)")
                        Text("W: \(standing.won), L: \(standing.lost), D: \(standing.draw)")
                            .font(.subheadline)
                            .foregroundColor(.gray)
                    }
                }
            }
        }
}

struct TopScorersTab: View {
    @StateObject var vm: TopScorersViewModelWrapper

    init(code: String) {
        _vm = StateObject(wrappedValue: TopScorersViewModelWrapper(code: code))
    }

    var body: some View {
        List(vm.state.scorers, id: \.player.id) { player in
            VStack(alignment: .leading) {
                Text("\(player.player.name)")
                Text("Goals: \(player.goals ?? 0)").font(.subheadline).foregroundColor(.gray)
            }
        }
    }
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
