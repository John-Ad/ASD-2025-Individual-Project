package jetbrains.kotlin.course.alias.team

import jetbrains.kotlin.course.alias.filestorage.FileStorageService
import jetbrains.kotlin.course.alias.util.Identifier
import jetbrains.kotlin.course.alias.util.IdentifierFactory
import org.springframework.stereotype.Service

@Service
class TeamService(private val fileStorageService: FileStorageService) {
    private lateinit var identifierFactory: IdentifierFactory;

    companion object {
        var teamsStorage: MutableMap<Identifier, Team> = mutableMapOf()
    }

    init {
        val (lastSavedTeamId, lastSavedCardId) = fileStorageService.loadLastAssignedIds();
        identifierFactory = IdentifierFactory(lastSavedTeamId ?: 0);

        if (teamsStorage.isEmpty()) {
            teamsStorage = fileStorageService.loadTeams();
        }
    }

    fun generateTeamsForOneRound(teamsNumber: Int): List<Team> {
        try {
            val teamsList = mutableListOf<Team>();
            for (i in 0 until teamsNumber) {
                val id = identifierFactory.uniqueIdentifier();
                val team = Team(id);
                teamsStorage[id] = team;
                teamsList.add(team);
            }

            fileStorageService.saveTeams(teamsStorage);

            if (teamsList.isNotEmpty()) {
                fileStorageService.saveLastAssignedTeamId(teamsList.last().id);
            }

            return teamsList;
        } catch (ex: Exception) {
            println(ex.message);
            return emptyList();
        }
    }
}
