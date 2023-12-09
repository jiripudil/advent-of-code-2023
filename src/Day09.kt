fun main() {
    fun compute(input: List<String>): Pair<Int, Int> {
        val results = input.map {
            val values = it.split(' ').map { n -> n.toInt() }
            var sequence = values

            val firstValues = mutableListOf(sequence.first())
            val lastValues = mutableListOf(sequence.last())

            do {
                sequence = sequence.zipWithNext { a, b -> b - a }
                firstValues.add(sequence.first())
                lastValues.add(sequence.last())
            } while (sequence.any { n -> n != 0 })

            Pair(
                lastValues.reduceRight { i, acc -> i + acc },
                firstValues.reduceRight { i, acc -> i - acc }
            )
        }

        return Pair(
            results.sumOf { it.first },
            results.sumOf { it.second }
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(compute(testInput).first == 114)

    val input = readInput("Day09")
    val (part1, part2) = compute(input)
    part1.println()
    part2.println()
}
