package jetbrains.kotlin.course.alias.filestorage

import jetbrains.kotlin.course.alias.results.GameResult
import jetbrains.kotlin.course.alias.team.Team
import jetbrains.kotlin.course.alias.util.Identifier
import org.springframework.stereotype.Service
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Service
class FileStorageService {
    companion object {
        private const val GAME_RESULTS_FILE = "game_results.json";
        private const val TEAMS_FILE = "teams.json";
        private const val LAST_ASSIGNED_IDS_FILE = "last_assigned_ids.json";
        private const val USED_WORDS_AND_CARDS_FILE = "used_words_and_cards.json";
    }

    private val json: Json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    init {
        ensureFileExists(GAME_RESULTS_FILE);
        ensureFileExists(TEAMS_FILE);
        ensureFileExists(LAST_ASSIGNED_IDS_FILE);
        ensureFileExists(USED_WORDS_AND_CARDS_FILE);
    }

    private final fun ensureFileExists(fileName: String): Unit {
        val file = File(fileName);
        if (file.exists() == false) {
            file.createNewFile();
        }
    }

    fun saveGameResults(data: List<GameResult>): Unit {
        val jsonString = json.encodeToString(data);
        File(GAME_RESULTS_FILE).writeText(jsonString);
    }

    fun saveTeams(data: MutableMap<Identifier, Team>): Unit {
        val jsonString = json.encodeToString(data);
        File(TEAMS_FILE).writeText(jsonString);
    }

    fun saveLastAssignedTeamId(lastAssignedTeamId: Identifier): Unit {
        val (lastSavedTeamId, lastSavedCardId) = loadLastAssignedIds();
        val jsonString = json.encodeToString(
            mapOf(
                "lastAssignedTeamId" to lastAssignedTeamId,
                "lastAssignedCardId" to (lastSavedCardId ?: 0),
            )
        );

        File(LAST_ASSIGNED_IDS_FILE).writeText(jsonString);
    }

    fun saveLastAssignedCardId(lastAssignedCardId: Identifier): Unit {
        val (lastSavedTeamId, lastSavedCardId) = loadLastAssignedIds();
        val jsonString = json.encodeToString(
            mapOf(
                "lastAssignedTeamId" to (lastSavedTeamId ?: 0),
                "lastAssignedCardId" to lastAssignedCardId,
            )
        );

        File(LAST_ASSIGNED_IDS_FILE).writeText(jsonString);
    }

    fun saveUsedWordsAndCards(usedWords: List<String>, usedCards: List<String>): Unit {
        val jsonString = json.encodeToString(
            mapOf(
                "usedWords" to usedWords,
                "usedCards" to usedCards
            )
        );
        File(USED_WORDS_AND_CARDS_FILE).writeText(jsonString);
    }

    fun loadGameResults(): List<GameResult> {
        try {
            val jsonString = File(GAME_RESULTS_FILE).readText();
            if (jsonString.isEmpty()) {
                return emptyList();
            }
            return json.decodeFromString(jsonString);
        } catch (ex: Exception) {
            println(ex.message);
            return emptyList();
        }
    }

    fun loadTeams(): MutableMap<Identifier, Team> {
        try {
            val jsonString = File(TEAMS_FILE).readText();
            if (jsonString.isEmpty()) {
                return mutableMapOf();
            }
            return json.decodeFromString(jsonString);
        } catch (ex: Exception) {
            println(ex.message);
            return mutableMapOf();
        }
    }

    fun loadLastAssignedIds(): Pair<Int?, Int?> {
        try {
            val jsonString = File(LAST_ASSIGNED_IDS_FILE).readText();
            if (jsonString.isEmpty()) {
                return Pair(0, 0);
            }
            val map = json.decodeFromString<Map<String, Int>>(jsonString);
            return Pair(map["lastAssignedTeamId"], map["lastAssignedCardId"]);
        } catch (ex: Exception) {
            println(ex.message);
            return Pair(0, 0);
        }
    }

    fun loadUsedWordsAndCards(): Pair<List<String>, List<String>> {
        try {
            val jsonString = File(USED_WORDS_AND_CARDS_FILE).readText();
            if (jsonString.isEmpty()) {
                return Pair(emptyList(), emptyList());
            }
            val map = json.decodeFromString<Map<String, List<String>>>(jsonString);
            return Pair(map["usedWords"] ?: emptyList(), map["usedCards"] ?: emptyList());
        } catch (ex: Exception) {
            println(ex.message);
            return Pair(emptyList(), emptyList());
        }
    }

}