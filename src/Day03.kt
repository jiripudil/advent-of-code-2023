fun main() {
    fun process(input: List<String>): List<Symbol> {
        val numbers = mutableListOf<Number>()
        val symbols = mutableListOf<Symbol>()

        input.forEachIndexed { y, line ->
            val numberMatches = Regex("\\d+").findAll(line)
            numberMatches.forEach { match ->
                numbers.add(Number(
                    match.value,
                    Position(match.range.first, y)
                ))
            }
        }

        input.forEachIndexed { y, line ->
            val symbolMatches = Regex("[^\\d.]{1}").findAll(line)
            symbolMatches.forEach { match ->
                val position = Position(match.range.start, y)
                symbols.add(Symbol(
                    match.value.first(),
                    numbers.filter { it.box.contains(position) }
                ))
            }
        }

        return symbols.toList()
    }

    fun part1(symbols: List<Symbol>): Int {
        val engineParts = symbols.flatMap { it.adjacentNumbers }.toSet()
        return engineParts.sumOf { it.value }
    }

    fun part2(symbols: List<Symbol>): Int {
        return symbols
            .filter { it.value == '*' && it.adjacentNumbers.size == 2 }
            .map { it.adjacentNumbers[0].value * it.adjacentNumbers[1].value }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(process(testInput)) == 4361)
    check(part2(process(testInput)) == 467835)

    val input = readInput("Day03")
    part1(process(input)).println()
    part2(process(input)).println()
}

data class Position(val x: Int, val y: Int)

data class BoundingBox(val x: Int, val y: Int, val width: Int, val height: Int) {
    constructor(x: Int, y: Int, length: Int) : this(x - 1, y - 1, length + 2, 3)

    fun contains(position: Position): Boolean {
        return position.x in x..(x + width - 1)
            && position.y in y..(y + height - 1)
    }
}

data class Number(private val rawValue: String, val position: Position) {
    val value = rawValue.toInt()
    val box = BoundingBox(position.x, position.y, rawValue.length)
}

data class Symbol(
    val value: Char,
    val adjacentNumbers: List<Number>,
)
