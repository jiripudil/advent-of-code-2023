fun main() {
    fun compute(input: List<String>): Pair<Int, Long> {
        val galaxies = buildSet {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    if (char == '#') {
                        this.add(Pair(x + 1, y + 1))
                    }
                }
            }
        }

        val width = input[0].length
        val emptyColumns = (1..width).filter { x -> galaxies.none { galaxy -> galaxy.first == x } }

        val height = input.size
        val emptyRows = (1..height).filter { y -> galaxies.none { galaxy -> galaxy.second == y } }

        val pairs = buildList {
            galaxies.forEachIndexed { index, a ->
                galaxies.drop(index + 1).forEach { b ->
                    this.add(Pair(a, b))
                }
            }
        }

        fun distanceBetween(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
            val x = listOf(a.first, b.first).sorted()
            val y = listOf(a.second, b.second).sorted()

            val diffX = x[1] - x[0] + emptyColumns.filter { it in x[0]..x[1] }.size
            val diffY = y[1] - y[0] + emptyRows.filter { it in y[0]..y[1] }.size

            return diffX + diffY
        }

        fun distanceBetweenExpanded(a: Pair<Int, Int>, b: Pair<Int, Int>): Long {
            val x = listOf(a.first, b.first).sorted()
            val y = listOf(a.second, b.second).sorted()

            val diffX = x[1] - x[0] + (emptyColumns.filter { it in x[0]..x[1] }.size * 999_999L)
            val diffY = y[1] - y[0] + (emptyRows.filter { it in y[0]..y[1] }.size * 999_999L)

            return diffX + diffY
        }

        return Pair(
            pairs.map { distanceBetween(it.first, it.second) }.sum(),
            pairs.map { distanceBetweenExpanded(it.first, it.second) }.sum()
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val (part1Test, part2Test) = compute(testInput)
    check(part1Test == 374)

    val input = readInput("Day11")
    val (part1, part2) = compute(input)
    part1.println()
    part2.println()
}
