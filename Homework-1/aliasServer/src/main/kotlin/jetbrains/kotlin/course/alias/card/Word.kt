package jetbrains.kotlin.course.alias.card

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Word(val word: String);