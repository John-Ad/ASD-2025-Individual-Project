package jetbrains.kotlin.course.alias.team

import jetbrains.kotlin.course.alias.util.Identifier
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Team(
    val id: Identifier,

    @EncodeDefault
    var points: Int = 0
) {
    @EncodeDefault
    val name: String = "Team#${id + 1}"
}
