fun main() {
    fun part1(input: List<String>): Int {
        val solutions = input.map { line ->
            val (row, groupsInput) = line.split(' ')
            val groups = groupsInput.split(',').map { it.toInt() }

            fun countValidSolutions(row: String, currentGroup: Int?, otherGroups: List<Int>): Int {
                if (row.isEmpty()) {
                    return if ((currentGroup == null || currentGroup == 0) && otherGroups.isEmpty()) 1 else 0
                }

                when (row.first()) {
                    '.' -> {
                        if (currentGroup != null && currentGroup > 0) {
                            return 0
                        }

                        return countValidSolutions(row.substring(1), null, otherGroups)
                    }
                    '#' -> {
                        if (currentGroup == 0) {
                            return 0
                        }

                        if (currentGroup == null) {
                            if (otherGroups.isEmpty()) {
                                return 0
                            }

                            return countValidSolutions(
                                row.substring(1),
                                otherGroups.first() - 1,
                                otherGroups.drop(1)
                            )
                        }

                        return countValidSolutions(
                            row.substring(1),
                            currentGroup - 1,
                            otherGroups
                        )
                    }
                    else -> {
                        val goodRow = '.' + row.substring(1)
                        val brokenRow = '#' + row.substring(1)
                        return countValidSolutions(goodRow, currentGroup, otherGroups) + countValidSolutions(brokenRow, currentGroup, otherGroups)
                    }
                }
            }

            countValidSolutions(row, null, groups)
        }

        return solutions.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
