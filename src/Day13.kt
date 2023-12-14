import kotlin.math.min

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

        val vertical = patterns.map { findVerticalReflection(it, fuzzy = true) }
        val horizontal = patterns.map { findHorizontalReflection(it, fuzzy = true) * 100 }

        return vertical.sum() + horizontal.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

fun findVerticalReflection(pattern: List<String>, fuzzy: Boolean = false): Int {
    val rotated = (0..<pattern[0].length).map { index ->
        pattern.reversed().map { line -> line[index] }.joinToString("")
    }

    return findHorizontalReflection(rotated, fuzzy)
}

fun findHorizontalReflection(pattern: List<String>, fuzzy: Boolean = false): Int {
    pattern.forEachIndexed { index, line ->
        if (reflects(pattern, index, fuzzy)) {
            return index + 1
        }
    }

    return 0
}

fun reflects(pattern: List<String>, index: Int, fuzzy: Boolean): Boolean {
    if (index == pattern.size - 1) {
        return false
    }

    var foundSmudge = false

    for (i in 1..pattern.size) {
        val backward = pattern.getOrNull(index - i + 1)
        val forward = pattern.getOrNull(index + i)

        if (backward == null || forward == null) {
            return !fuzzy || foundSmudge
        }

        if (backward != forward) {
            if (fuzzy && !foundSmudge && levenshtein(backward, forward) == 1) {
                foundSmudge = true
            } else {
                return false
            }
        }
    }

    return !fuzzy || foundSmudge
}

fun levenshtein(a: String, b: String): Int {
    val m = a.length
    val n = b.length
    val t = Array(m + 1) { IntArray(n + 1) }

    for (i in 1..m) {
        t[i][0] = i
    }

    for (j in 1..n) {
        t[0][j] = j
    }

    var cost: Int
    for (i in 1..m) {
        for (j in 1..n) {
            cost = if (a[i - 1] == b[j - 1]) 0 else 1
            t[i][j] = min(
                min(
                    t[i - 1][j] + 1,
                    t[i][j - 1] + 1
                ),
                t[i - 1][j - 1] + cost
            )
        }
    }

    return t[m][n]
}
