fun main() {
    fun part1(input: List<String>): Int {
        val hands = input.map { line ->
            val (handInput, bidInput) = line.split(" ")
            val hand = Hand.parse(handInput)
            val bid = bidInput.toInt()
            Pair(hand, bid)
        }

        val byRank = hands.sortedBy { it.first }
        return byRank.mapIndexed { index, pair -> (index + 1) * pair.second }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    // check(part2(testInput) == 0)

    val input = readInput("Day07")
    part1(input).println()
    // part2(input).println()
}

class Hand(private val cards: List<Card>) : Comparable<Hand> {
    init {
        require(cards.size == 5)
    }

    val strength: Int get() {
        val counts = cards
            .groupBy { it }
            .map { (_, list) -> list.size }
            .sorted()
            .reversed()

        return when {
            counts[0] == 5 -> 7 // five of a kind
            counts[0] == 4 -> 6 // four of a kind
            counts[0] == 3 && counts[1] == 2 -> 5 // full house
            counts[0] == 3 -> 4 // three of a kind
            counts[0] == 2 && counts[1] == 2 -> 3 // two pair
            counts[0] == 2 -> 2 // one pair
            else -> 1 // high card
        }
    }

    override fun compareTo(other: Hand): Int {
        val strengths = strength - other.strength
        if (strengths != 0) {
            return strengths
        }

        cards.forEachIndexed { index, card ->
            val otherCard = other.cards[index]
            val diff = card.compareTo(otherCard)
            if (diff != 0) {
                return diff
            }
        }

        return 0
    }

    companion object {
        fun parse(input: String): Hand {
            val cards = input.chunked(1).map { Card.from(it) }
            return Hand(cards)
        }
    }

    data class Card(private val value: Int) {
        fun compareTo(other: Card): Int {
            return value.compareTo(other.value)
        }

        companion object {
            fun from(symbol: String): Card {
                return when (symbol) {
                    "2", "3", "4", "5", "6", "7", "8", "9" -> Card(symbol.toInt())
                    "T" -> Card(10)
                    "J" -> Card(11)
                    "Q" -> Card(12)
                    "K" -> Card(13)
                    "A" -> Card(14)
                    else -> throw InvalidInput()
                }
            }
        }
    }
}
