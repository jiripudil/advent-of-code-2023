import kotlin.math.pow

fun main() {
    fun part1(cards: List<Card>): Int {
        return cards
            .map { it.points }
            .sum()
    }

    fun part2(cards: List<Card>): Int {
        cards.forEachIndexed { index, card ->
            if (card.noOfWinningNumbers == 0) {
                return@forEachIndexed
            }

            (index + 1 .. (index + card.noOfWinningNumbers)).forEach { cards.getOrNull(it)?.copy(card.copies) }
        }

        return cards.sumOf { it.copies }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val testCards = testInput.map(Card::parse)
    check(part1(testCards) == 13)
    check(part2(testCards) == 30)

    val input = readInput("Day04")
    val cards = input.map(Card::parse)
    part1(cards).println()
    part2(cards).println()
}

class Card(val id: Int, private val winningNumbers: List<Int>, private val cardNumbers: List<Int>) {
    var copies = 1
        private set

    val noOfWinningNumbers: Int
        get() = cardNumbers.intersect(winningNumbers.toSet()).size

    val points: Int
        get() {
            if (noOfWinningNumbers == 0) {
                return 0
            }

            return 2f.pow(noOfWinningNumbers - 1).toInt()
        }

    fun copy(n: Int) {
        copies += n
    }

    companion object {
        fun parse(line: String): Card {
            val match = Regex("^Card\\s+(\\d+): (.+)$").matchEntire(line) ?: throw InvalidInput()
            val id = match.groups[1]?.value ?: throw InvalidInput()
            val numbers = match.groups[2]?.value ?: throw InvalidInput()

            val (rawWinningNumbers, rawCardNumbers) = numbers.split('|')
            val winningNumbers = rawWinningNumbers.trim().split(Regex("\\s+")).map { it.toInt() }
            val cardNumbers = rawCardNumbers.trim().split(Regex("\\s+")).map { it.toInt() }

            return Card(id.toInt(), winningNumbers, cardNumbers)
        }
    }
}
