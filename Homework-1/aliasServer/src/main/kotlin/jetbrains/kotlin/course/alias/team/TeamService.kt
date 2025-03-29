package jetbrains.kotlin.course.alias.team

import jetbrains.kotlin.course.alias.filestorage.FileStorageService
import jetbrains.kotlin.course.alias.util.Identifier
import jetbrains.kotlin.course.alias.util.IdentifierFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TeamService(
    private val fileStorageService: FileStorageService,
    @Value("\${saveState:false}") private val saveState: String
) {
    private val identifierFactory: IdentifierFactory = IdentifierFactory(0);

    constructor() : this(FileStorageService(), "false")

    companion object {
        var teamsStorage: MutableMap<Identifier, Team> = mutableMapOf()
    }

    init {
        if (saveState == "true") {
            val (lastSavedTeamId, lastSavedCardId) = fileStorageService.loadLastAssignedIds();
            if (lastSavedTeamId != null) {
                // add 1 to the last saved id because when
                // generating a unique id, the current count is
                // returned and then incremented. Not incrementing
                // lastSavedTeamId will cause the next id generated
                // to be the same as the last saved id
                identifierFactory.setCounter(lastSavedTeamId + 1);
            }


            if (teamsStorage.isEmpty()) {
                teamsStorage = fileStorageService.loadTeams();
            }
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

            if (saveState == "true") {
                fileStorageService.saveTeams(teamsStorage);

                if (teamsList.isNotEmpty()) {
                    fileStorageService.saveLastAssignedTeamId(teamsList.last().id);
                }
            }

            return teamsList;
        } catch (ex: Exception) {
            println(ex.message);
            return emptyList();
        }
    }
}
