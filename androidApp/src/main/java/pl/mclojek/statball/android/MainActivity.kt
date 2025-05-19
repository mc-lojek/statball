package pl.mclojek.statball.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import pl.mclojek.statball.CompetitionMatchesViewModel
import pl.mclojek.statball.CompetitionsViewModel
import pl.mclojek.statball.KtorClient
import pl.mclojek.statball.MatchDetailsViewModel
import pl.mclojek.statball.StandingsViewModel
import pl.mclojek.statball.TeamDetailsViewModel
import pl.mclojek.statball.TeamMatchesViewModel
import pl.mclojek.statball.TopScorersViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingView()
                }
            }
        }
    }
}

@Composable
fun GreetingView() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = CompetitionsDestination
    ) {
        composable<CompetitionsDestination> {
            CompetitionsScreen {
                navController.navigate(CompetitionDetailsDestination(it))
            }
        }
        composable<CompetitionDetailsDestination> {
            CompetitionDetailsScreen(
                it.toRoute(),
                onMatchSelected = { navController.navigate(MatchDetailsDestination(it)) },
                onTeamSelected = { navController.navigate(TeamOverviewDestination(it)) })
        }
        composable<MatchDetailsDestination> { MatchDetailsScreen(it.toRoute()) }
        composable<TeamOverviewDestination> { TeamOverviewScreen(it.toRoute()) }
    }
}

@Serializable
object CompetitionsDestination

@Composable
fun CompetitionsScreen(
    onCompetitionSelected: (String) -> Unit,
) {

    val viewModel = remember { CompetitionsViewModel(KtorClient()) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.competitions) { competition ->
            Text(competition.name.orEmpty(), modifier = Modifier.clickable {
                onCompetitionSelected(competition.code.orEmpty())
            })
        }
    }
}

@Serializable
data class CompetitionDetailsDestination(val code: String)

@Composable
fun CompetitionDetailsScreen(
    args: CompetitionDetailsDestination,
    onMatchSelected: (Int) -> Unit,
    onTeamSelected: (Int) -> Unit
) {

    val tabs = listOf("Standings", "Top Scorers", "Matches")

    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            title,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> StandingsScreen(args, onTeamSelected)
                1 -> TopScorersScreen(args)
                2 -> CompetitionMatchesScreen(args, onMatchSelected)
            }
        }
    }
}

@Composable
fun StandingsScreen(args: CompetitionDetailsDestination, onTeamSelected: (Int) -> Unit) {

    val viewModel = remember { StandingsViewModel(args.code, KtorClient()) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.standings) { standing ->
            Text(
                "${standing.position}. ${standing.team.name} ${standing.won}W ${standing.lost}L ${standing.draw}D",
                modifier = Modifier.clickable { onTeamSelected(standing.team.id) })
        }
    }
}

@Composable
fun TopScorersScreen(args: CompetitionDetailsDestination) {

    val viewModel = remember { TopScorersViewModel(args.code, KtorClient()) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(state.scorers) { index, scorer ->
            Text("${index + 1}. ${scorer.player.name} ${scorer.goals ?: "-"}G ${scorer.assists ?: "-"}A")
        }
    }
}

@Composable
fun CompetitionMatchesScreen(
    args: CompetitionDetailsDestination,
    onMatchSelected: (Int) -> Unit
) {

    val viewModel = remember { CompetitionMatchesViewModel(args.code, KtorClient()) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(state.matches) { index, match ->
            Text(
                "${index + 1}. ${match.homeTeam.name} vs ${match.awayTeam.name}",
                modifier = Modifier.clickable { onMatchSelected(match.id) })
        }
    }
}

@Serializable
data class MatchDetailsDestination(val matchId: Int)

@Composable
fun MatchDetailsScreen(args: MatchDetailsDestination) {
    val viewModel = remember { MatchDetailsViewModel(args.matchId, KtorClient()) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(state.matchDetails?.homeTeam?.name.orEmpty())
        Text(state.matchDetails?.awayTeam?.name.orEmpty())
        Text(state.matchDetails?.score?.fullTime?.home.toString())
    }
}

@Serializable
data class TeamOverviewDestination(val teamId: Int)

@Composable
fun TeamOverviewScreen(args: TeamOverviewDestination) {

    val tabs = listOf("Squad", "Matches")

    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            title,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> TeamDetailsScreen(args)
                1 -> TeamMatchesScreen(args)
            }
        }
    }
}

@Composable
fun TeamDetailsScreen(args: TeamOverviewDestination) {
    val viewModel = remember { TeamDetailsViewModel(args.teamId, KtorClient()) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(state.teamDetails?.name.orEmpty())
        Text(state.teamDetails?.name.orEmpty())
        Text(state.teamDetails?.name.orEmpty())
        Text(state.teamDetails?.name.orEmpty())
    }
}

@Composable
fun TeamMatchesScreen(args: TeamOverviewDestination) {
    val viewModel = remember { TeamMatchesViewModel(args.teamId, KtorClient()) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.teamMatches?.matches.orEmpty()) { match ->
            Text("${match.homeTeam.name} vs ${match.awayTeam.name}")
        }
    }
}
