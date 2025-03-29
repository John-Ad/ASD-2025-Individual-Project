package jetbrains.kotlin.course.alias.results

import jetbrains.kotlin.course.alias.filestorage.FileStorageService
import jetbrains.kotlin.course.alias.team.Team
import jetbrains.kotlin.course.alias.team.TeamService
import kotlinx.serialization.Serializable
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

typealias GameResult = List<Team>;

@Service
class GameResultsService(
    private val fileStorageService: FileStorageService,
    @Value("\${saveState:false}") private val saveState: String
) {

    constructor() : this(FileStorageService(), "false")

    companion object {
        val gameHistory: MutableList<GameResult> = mutableListOf()
    }

    init {
        if (saveState == "true") {
            if (gameHistory.isEmpty()) {
                gameHistory.addAll(fileStorageService.loadGameResults())
            }
        }
    }

    fun saveGameResults(result: GameResult): Unit {
        if (result.isEmpty())
            throw IllegalArgumentException("Game result is empty")
        if (result.any { TeamService.teamsStorage.containsKey(it.id) == false })
            throw IllegalArgumentException("Unknown team in game result")

        gameHistory.add(result)

        if (saveState == "true") {
            fileStorageService.saveGameResults(gameHistory)
        }
    }

    fun getAllGameResults(): List<GameResult> = gameHistory.reversed()
}
