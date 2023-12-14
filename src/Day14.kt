fun main() {
    fun part1(input: List<String>): Int {
        val rocks = mutableMapOf<Pair<Int, Int>, Char>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char != '.') {
                    rocks.set(Pair(x, y), char)
                }
            }
        }

        for (x in 0..<input[0].length) {
            for (y in 1..<input.size) {
                val coordinates = Pair(x, y)
                val rock = rocks[coordinates]
                if (rock == '#' || rock == null) {
                    continue
                }

                var checkY = y - 1
                while (checkY >= 0 && !rocks.contains(Pair(x, checkY))) {
                    checkY -= 1
                }

                if (checkY < y - 1) {
                    rocks.remove(coordinates)
                    rocks.set(Pair(x, checkY + 1), rock)
                }
            }
        }

        return rocks.entries.sumOf { (coordinates, rock) ->
            if (rock != 'O') 0 else input.size - coordinates.second
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
