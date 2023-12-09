fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input.first().chunked(1)
        val nodes = input.drop(2).map {
            val match = Regex("(\\w+) = \\((\\w+), (\\w+)\\)").find(it) ?: throw InvalidInput()
            Pair(match.groupValues[1], Pair(match.groupValues[2], match.groupValues[3]))
        }.toMap()

        var steps = 0
        var position = nodes.getValue("AAA")

        while (true) {
            for (instruction in instructions) {
                val nextPosition = when (instruction) {
                    "L" -> position.first
                    "R" -> position.second
                    else -> throw InvalidInput()
                }

                steps++
                position = nodes.getValue(nextPosition)

                if (nextPosition == "ZZZ") {
                    return steps
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    //check(part2(testInput) == 0)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
