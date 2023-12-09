fun main() {
    fun part1(input: List<String>): Int {
        val results = input.map {
            val values = it.split(' ').map { n -> n.toInt() }
            var sequence = values

            val lastValues = mutableListOf(sequence.last())

            do {
                sequence = sequence.zipWithNext { a, b -> b - a }
                lastValues.add(sequence.last())
            } while (sequence.any { n -> n != 0 })

            lastValues.reduceRight { i, acc -> i + acc }
        }

        return results.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09")
    part1(input).println()
    //part2(input).println()
}
