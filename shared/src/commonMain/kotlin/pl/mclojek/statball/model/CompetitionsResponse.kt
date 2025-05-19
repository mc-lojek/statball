package pl.mclojek.statball.model

import kotlinx.serialization.Serializable

@Serializable
data class CompetitionsResponse(
    val competitions: List<Competition>,
)

@Serializable
data class Competition(
    val id: Int,
    val name: String?,
    val code: String?,
    val emblem: String?,
)

@Serializable
data class Score(
    val winner: String?,
    val duration: String,
    val fullTime: ScoreDetails,
    val halfTime: ScoreDetails
)

@Serializable
data class ScoreDetails(
    val home: Int?,
    val away: Int?,
)

@Serializable
data class FootballStandingsResponse(
    val competition: Competition,
    val standings: List<StandingTable>
)

@Serializable
data class StandingTable(
    val table: List<TeamStanding>
)

@Serializable
data class TeamStanding(
    val position: Int,
    val team: Team,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int
)

@Serializable
data class FootballScorersResponse(
    val competition: Competition,
    val scorers: List<Scorer>
)

@Serializable
data class Scorer(
    val player: Player,
    val team: Team,
    val playedMatches: Int,
    val goals: Int?,
    val assists: Int?,
)

@Serializable
data class Player(
    val id: Int,
    val name: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: String,
    val nationality: String,
    val section: String? = null,
    val shirtNumber: Int? = null,
)

@Serializable
data class Team(
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String,
    val crest: String,
    val clubColors: String? = null,
    val venue: String? = null,
)

@Serializable
data class FootballMatchesResponse(
    val competition: Competition,
    val matches: List<Match>
)

@Serializable
data class FootballMatch(
    val competition: Competition,
    val id: Int,
    val utcDate: String,
    val status: String,
    val venue: String?,
    val matchday: Int,
    val lastUpdated: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score,
    val referees: List<Referee>
)

@Serializable
data class Referee(
    val id: Int,
    val name: String,
    val type: String,
    val nationality: String
)

@Serializable
data class FootballTeam(
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String,
    val crest: String,
    val clubColors: String,
    val venue: String,
    val coach: Coach,
    val squad: List<Player>,
)

@Serializable
data class Coach(
    val id: Int,
    val firstName: String?,
    val lastName: String,
    val name: String,
    val dateOfBirth: String,
    val nationality: String,
)

@Serializable
data class FootballMatchResponse(
    val resultSet: ResultSet,
    val matches: List<Match>
)

@Serializable
data class ResultSet(
    val count: Int,
    val competitions: String,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int
)

@Serializable
data class Match(
    val competition: Competition,
    val id: Int,
    val utcDate: String,
    val status: String,
    val matchday: Int,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score,
)
