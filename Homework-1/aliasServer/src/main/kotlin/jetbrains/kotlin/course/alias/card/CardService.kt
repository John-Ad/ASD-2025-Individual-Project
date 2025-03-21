package jetbrains.kotlin.course.alias.card

import jetbrains.kotlin.course.alias.util.IdentifierFactory
import jetbrains.kotlin.course.alias.util.words
import org.springframework.stereotype.Service

@Service
class CardService {
    private val identifierFactory: IdentifierFactory = IdentifierFactory()
    private val cards: List<Card> = generateCards()

    companion object {
        const val WORDS_IN_CARD = 4;
        var cardsAmount = words.size / WORDS_IN_CARD
    }

    private fun generateCards(): List<Card> {
        return words
            .shuffled()
            .chunked(WORDS_IN_CARD)
            .take(cardsAmount)
            .map {
                Card(
                    identifierFactory.uniqueIdentifier(),
                    it.toWords()
                )
            }
    }

    private fun List<String>.toWords(): List<Word> {
        return this.map { Word(it) }.toList()
    }

    fun getCardByIndex(index: Int): Card {
        if (index < 0 || index >= cards.size) {
            throw IllegalArgumentException("Index is out of bounds")
        }

        return cards[index]
    }
}
