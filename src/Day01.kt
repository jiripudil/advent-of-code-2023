fun main() {
    fun part1(input: List<String>): Int {
        fun extractCalibrationNumber(line: String): Int {
            val first = line.first { char -> char.isDigit() }.digitToInt()
            val last = line.last { char -> char.isDigit() }.digitToInt()
            return first * 10 + last
        }

        return input.sumOf(::extractCalibrationNumber)
    }

    fun part2(input: List<String>): Int {
        fun extractCalibrationNumber(line: String): Int {
            val normalizedLine = line.mapIndexed { index, char -> when {
                line.substring(index).startsWith("one") -> '1'
                line.substring(index).startsWith("two") -> '2'
                line.substring(index).startsWith("three") -> '3'
                line.substring(index).startsWith("four") -> '4'
                line.substring(index).startsWith("five") -> '5'
                line.substring(index).startsWith("six") -> '6'
                line.substring(index).startsWith("seven") -> '7'
                line.substring(index).startsWith("eight") -> '8'
                line.substring(index).startsWith("nine") -> '9'
                else -> char
            }}

            val first = normalizedLine.first { char -> char.isDigit() }.digitToInt()
            val last = normalizedLine.last { char -> char.isDigit() }.digitToInt()
            return first * 10 + last
        }

        return input.sumOf(::extractCalibrationNumber)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
