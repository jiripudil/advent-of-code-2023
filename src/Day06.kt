fun main() {
    fun part1(input: List<String>): Long {
        return parseRaces(input)
            .map { it.computePlaysThatBeatRecord() }
            .reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        return parseRace(input).computePlaysThatBeatRecord()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

data class Race(val time: Long, val bestDistance: Long) {
    fun computePlaysThatBeatRecord(): Long {
        var playsThatBeatRecord = 0
        for (raceTime in 0..<time) {
            val springTime = time - raceTime
            val distance = springTime * raceTime
            if (distance > bestDistance) {
                playsThatBeatRecord++
            }
        }

        return playsThatBeatRecord.toLong()
    }
}

val ws = Regex("\\s+")

fun parseRaces(input: List<String>): List<Race> {
    val (timesInput, distancesInput) = input
    val times = timesInput.split(ws).drop(1).map { it.toLong() }
    val distances = distancesInput.split(ws).drop(1).map { it.toLong() }

    return times.zip(distances).map { (time, bestDistance) -> Race(time, bestDistance) }
}

fun parseRace(input: List<String>): Race {
    val (timesInput, distancesInput) = input
    val time = timesInput.split(ws, limit = 2).last().replace(ws, "").toLong()
    val distance = distancesInput.split(ws, limit = 2).last().replace(ws, "").toLong()

    return Race(time, distance)
}
