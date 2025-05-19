package pl.mclojek.statball

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pl.mclojek.statball.model.CompetitionsResponse
import pl.mclojek.statball.model.FootballMatch
import pl.mclojek.statball.model.FootballMatchResponse
import pl.mclojek.statball.model.FootballMatchesResponse
import pl.mclojek.statball.model.FootballScorersResponse
import pl.mclojek.statball.model.FootballStandingsResponse
import pl.mclojek.statball.model.FootballTeam

class KtorClient {

    fun getClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }
                )
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 3000
                connectTimeoutMillis = 3000
                requestTimeoutMillis = 3000
            }

            install(DefaultRequest) {
                url {
                    host = "api.football-data.org/v4"
                    protocol = URLProtocol.HTTPS
                    contentType(ContentType.Application.Json)
                }

                header("X-Auth-Token", "daffd794374643538c61fab699d3f6c1")
            }
        }
    }

    suspend fun getCompetitions(): CompetitionsResponse {
        return getClient().get("/competitions").body()
    }

    suspend fun getStandings(code: String): FootballStandingsResponse {
        return getClient().get("/competitions/${code}/standings")
            .also { println(it.bodyAsText()) }
            .also { println(it.request.headers) }
            .body()
    }

    suspend fun getScorers(code: String): FootballScorersResponse {
        return getClient().get("/competitions/${code}/scorers")
            .also { println(it.bodyAsText()) }
            .also { println(it.request.headers) }
            .body()
    }

    suspend fun getMatches(code: String): FootballMatchesResponse {
        return getClient().get("/competitions/${code}/matches")
            .also { println(it.bodyAsText()) }
            .also { println(it.request.headers) }
            .body()
    }

    suspend fun getMatch(id: Int): FootballMatch {
        return getClient().get("/matches/${id}")
            .also { println(it.bodyAsText()) }
            .also { println(it.request.headers) }
            .body()
    }

    suspend fun getTeam(id: Int): FootballTeam {
        return getClient().get("/teams/${id}")
            .also { println(it.bodyAsText()) }
            .also { println(it.request.headers) }
            .body()
    }

    suspend fun getTeamMatches(id: Int): FootballMatchResponse {
        return getClient().get("/teams/${id}/matches")
            .also { println(it.bodyAsText()) }
            .also { println(it.request.headers) }
            .body()
    }
}