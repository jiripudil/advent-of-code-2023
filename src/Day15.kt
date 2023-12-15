fun main() {
    fun hash(s: String) = s.fold(0) { acc, char -> ((acc + char.code) * 17) % 256 }

    fun part1(input: String): Int {
        val segments = input.split(',')
        return segments.sumOf { hash(it) }
    }

    fun part2(input: String): Int {
        return input.length
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test").first()
    check(part1(testInput) == 1320)

    val input = readInput("Day15").first()
    part1(input).println()
    part2(input).println()
}
