package jetbrains.kotlin.course.alias.team

import jetbrains.kotlin.course.alias.util.Identifier
import jetbrains.kotlin.course.alias.util.IdentifierFactory
import org.springframework.stereotype.Service

@Service
class TeamService {
    private val identifierFactory: IdentifierFactory = IdentifierFactory();

    companion object {
        var teamsStorage: MutableMap<Identifier, Team> = mutableMapOf()
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

            return teamsList;
        } catch (ex: Exception) {
            println(ex.message);
            return emptyList();
        }
    }
}
