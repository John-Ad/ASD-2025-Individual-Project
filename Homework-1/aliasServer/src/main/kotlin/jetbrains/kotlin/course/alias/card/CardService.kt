package jetbrains.kotlin.course.alias.card

import jetbrains.kotlin.course.alias.filestorage.FileStorageService
import jetbrains.kotlin.course.alias.util.IdentifierFactory
import jetbrains.kotlin.course.alias.util.words
import org.springframework.stereotype.Service

@Service
class CardService(private val fileStorageService: FileStorageService) {
    private lateinit var identifierFactory: IdentifierFactory;
    private lateinit var cards: List<Card>;

    init {
        val (lastSavedTeamId, lastSavedCardId) = fileStorageService.loadLastAssignedIds()
        identifierFactory = IdentifierFactory(lastSavedCardId ?: 0);
        cards = generateCards();
    }

    companion object {
        private const val WORDS_IN_CARD = 4;
        var cardsAmount = words.size / WORDS_IN_CARD
    }

    private fun generateCards(): List<Card> {
        val (usedWords, usedCards) = fileStorageService.loadUsedWordsAndCards()

        var returnData: List<Card> = emptyList()

        // when all words are used, clear the used words and cards
        // and skip filtering logic below
        if (usedWords.size == words.size) {
            fileStorageService.saveUsedWordsAndCards(emptyList(), emptyList())
            returnData = words
                .shuffled()
                .chunked(WORDS_IN_CARD)
                .take(cardsAmount)
                .map {
                    Card(
                        identifierFactory.uniqueIdentifier(),
                        it.toWords()
                    )
                };

            if (returnData.isNotEmpty()) {
                fileStorageService.saveLastAssignedCardId(returnData.last().id);
            }

            return returnData;
        }

        // shuffle all unused words and put the used words (shuffled) at the end
        val wordsWithoutUsedWords = words
            .filter {
                !usedWords.contains(it)
            }
            .shuffled()
            .toMutableList();
        wordsWithoutUsedWords.addAll(usedWords.shuffled())

        returnData = wordsWithoutUsedWords
            .chunked(WORDS_IN_CARD)
            .take(cardsAmount)
            .map {
                Card(
                    identifierFactory.uniqueIdentifier(),
                    it.toWords()
                )
            }

        if (returnData.isNotEmpty()) {
            fileStorageService.saveLastAssignedCardId(returnData.last().id);
        }

        return returnData;
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
