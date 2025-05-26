package pl.mclojek.statball.util

import pl.mclojek.statball.CompetitionMatchesViewModel
import pl.mclojek.statball.CompetitionsViewModel
import pl.mclojek.statball.KtorClient
import pl.mclojek.statball.MatchDetailsViewModel
import pl.mclojek.statball.StandingsViewModel
import pl.mclojek.statball.TeamDetailsViewModel
import pl.mclojek.statball.TeamMatchesViewModel
import pl.mclojek.statball.TopScorersViewModel

object ViewModelFactory {
    fun createCompetitionsViewModel(): CompetitionsViewModel {
        return CompetitionsViewModel(KtorClient())
    }

    fun createCompetitionMatchesViewModel(code: String): CompetitionMatchesViewModel {
        return CompetitionMatchesViewModel(code, KtorClient())
    }

    fun createStandingsViewModel(code: String): StandingsViewModel {
        return StandingsViewModel(code, KtorClient())
    }

    fun createTopScorersViewModel(code: String): TopScorersViewModel {
        return TopScorersViewModel(code, KtorClient())
    }

    fun createMatchDetailsViewModel(id: Int): MatchDetailsViewModel {
        return MatchDetailsViewModel(id, KtorClient())
    }

    fun createTeamDetailsViewModel(id: Int): TeamDetailsViewModel {
        return TeamDetailsViewModel(id, KtorClient())
    }

    fun createTeamMatchesViewModel(id: Int): TeamMatchesViewModel {
        return TeamMatchesViewModel(id, KtorClient())
    }
}