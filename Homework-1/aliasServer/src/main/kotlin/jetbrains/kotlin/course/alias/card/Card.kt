package jetbrains.kotlin.course.alias.card

import jetbrains.kotlin.course.alias.util.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class Card(val id: Identifier, val words: List<Word>)
