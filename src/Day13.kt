fun main() {
    fun part1(input: List<String>): Int {
        val patterns = mutableListOf<List<String>>()
        val pattern = mutableListOf<String>()
        input.forEach { line ->
            if (line == "") {
                patterns.add(pattern.toList())
                pattern.clear()
            } else {
                pattern += line
            }
        }
        patterns.add(pattern)

        val vertical = patterns.map { findVerticalReflection(it) }
        val horizontal = patterns.map { findHorizontalReflection(it) * 100 }

        return vertical.sum() + horizontal.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    //check(part1(testInput) == 405)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

fun findVerticalReflection(pattern: List<String>): Int {
    val rotated = (0..<pattern[0].length).map { index ->
        pattern.reversed().map { line -> line[index] }.joinToString("")
    }

    return findHorizontalReflection(rotated)
}

fun findHorizontalReflection(pattern: List<String>): Int {
    pattern.forEachIndexed { index, line ->
        if (reflects(pattern, index)) {
            return index + 1
        }
    }

    return 0
}

fun reflects(pattern: List<String>, index: Int): Boolean {
    if (index == pattern.size - 1) {
        return false
    }

    for (i in 1..pattern.size) {
        val backward = pattern.getOrNull(index - i + 1)
        val forward = pattern.getOrNull(index + i)

        if (backward == null || forward == null) {
            return true
        }

        if (backward != forward) {
            return false
        }
    }

    return true
}
