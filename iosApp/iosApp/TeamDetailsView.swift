import SwiftUI
import shared

struct TeamDetailsView: View {
    let id: Int32
    @State private var selectedTab = 0

    var body: some View {
        VStack {
            HStack {
                ForEach(0..<2) { index in
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
                SquadTab(id: id).tag(0)
                TeamMatchesTab(id: id).tag(1)
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
        }
        .navigationTitle("Team Details")
    }

    private func tabTitle(for index: Int) -> String {
        switch index {
        case 0: return "Squad"
        case 1: return "Matches"
        default: return "Tab"
        }
    }
}

import SwiftUI

struct SquadTab: View {
    @StateObject var vm: TeamDetailsViewModelWrapper

    init(id: Int32) {
        _vm = StateObject(wrappedValue: TeamDetailsViewModelWrapper(id: id))
    }

    var body: some View {
        if let team = vm.state.teamDetails {
            let playerModels = team.squad.map { PlayerModel(player: $0) }

            List {
                // Section for team details
                Section {
                    VStack(alignment: .center, spacing: 12) {
                        // Crest image (if URL is valid)
                        if let crestURL = URL(string: team.crest) {
                            AsyncImage(url: crestURL) { phase in
                                switch phase {
                                case .empty:
                                    ProgressView()
                                case .success(let image):
                                    image
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 100, height: 100)
                                case .failure:
                                    Image(systemName: "photo")
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 100, height: 100)
                                @unknown default:
                                    EmptyView()
                                }
                            }
                        }

                        Text(team.name)
                            .font(.title)
                            .bold()

                        Text("Arena: \(team.venue)")
                            .font(.subheadline)
                            .foregroundColor(.gray)

                        Text("Coach: \(team.coach.name)")
                            .font(.subheadline)
                            .foregroundColor(.gray)
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding()
                }

                // Section for squad players
                Section {
                    ForEach(playerModels) { player in
                        VStack(alignment: .leading) {
                            Text(player.name)
                            if let shirtNumber = player.shirtNumber {
                                Text("Shirt #: \(shirtNumber)")
                                    .font(.subheadline)
                                    .foregroundColor(.gray)
                            }
                        }
                    }
                }
            }
        } else {
            ProgressView("Loading squad…")
        }
    }
}


import SwiftUI

struct TeamMatchesTab: View {
    @StateObject var vm: TeamMatchesViewModelWrapper
    let teamId: Int32  // <-- Add this property
    
    init(id: Int32) {
        self.teamId = id
        _vm = StateObject(wrappedValue: TeamMatchesViewModelWrapper(id: id))
    }

    var body: some View {
        if let matches = vm.state.teamMatches?.matches {
            // Assuming matches is [Match]
            List(matches, id: \.id) { match in
                NavigationLink(destination: MatchDetailsView(id: match.id)) {
                    VStack(alignment: .leading) {
                        // Determine the opponent team name
                        let opponentName = match.homeTeam.id == teamId
                            ? match.awayTeam.name
                            : match.homeTeam.name

                        // Format the date string (assuming utcDate is ISO8601)
                        let date = iso8601ToDate(match.utcDate)
                        let dateString = dateFormatter.string(from: date)

                        Text("vs " + opponentName ?? "Unknown Opponent")
                            .font(.headline)
                        Text(dateString)
                            .font(.subheadline)
                            .foregroundColor(.gray)
                    }
                }
            }
        } else {
            ProgressView("Loading matches…")
        }
    }

    // Helper function to convert ISO8601 string to Date
    private func iso8601ToDate(_ isoString: String) -> Date {
        let formatter = ISO8601DateFormatter()
        return formatter.date(from: isoString) ?? Date()
    }

    // Date formatter for display (e.g., "May 26, 2025")
    private var dateFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        return formatter
    }
}
