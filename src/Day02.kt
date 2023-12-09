fun main() {
    fun part1(games: List<Game>): Int {
        val maximums = mapOf(
            Cube.red to 12,
            Cube.green to 13,
            Cube.blue to 14,
        )

        return games
            .filter { it.isPossible(maximums) }
            .sumOf { it.id }
    }

    fun part2(games: List<Game>): Int {
        return games
            .map { it.getMinimums().values.reduce { acc, i -> acc * i } }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val testGames = testInput.map(Game::parse)
    check(part1(testGames) == 8)
    check(part2(testGames) == 2286)

    val input = readInput("Day02")
    val games = input.map(Game::parse)
    part1(games).println()
    part2(games).println()
}

enum class Cube {
    red,
    green,
    blue
}

class Game(val id: Int, private val showings: List<Showing>) {
    fun isPossible(maximums: Map<Cube, Int>) = showings.all { it.isPossible(maximums) }

    fun getMinimums(): Map<Cube, Int> {
        return mapOf(
            Cube.red to showings.maxOf { it.cubes.getOrDefault(Cube.red, 0) },
            Cube.green to showings.maxOf { it.cubes.getOrDefault(Cube.green, 0) },
            Cube.blue to showings.maxOf { it.cubes.getOrDefault(Cube.blue, 0) },
        )
    }

    companion object {
        fun parse(line: String): Game {
            val (identifier, showingsInput) = line.split(':', limit = 2)

            val identifierMatch = Regex("^Game (\\d+)$").matchEntire(identifier) ?: throw InvalidInput()
            val id = identifierMatch.groups[1]?.value?.toInt() ?: throw InvalidInput()

            val showings = showingsInput.trim().split(';').map { Showing.parse(it) }

            return Game(id, showings)
        }
    }
}

class Showing(val cubes: Map<Cube, Int>) {
    fun isPossible(maximums: Map<Cube, Int>): Boolean {
        return maximums.all { (cube, maximum) -> cubes.getOrDefault(cube, 0) <= maximum }
    }

    companion object {
        fun parse(line: String): Showing {
            val cubes = buildMap {
                line.split(',').forEach {
                    val matches = Regex("^(\\d+) (\\w+)$").matchEntire(it.trim()) ?: throw InvalidInput()
                    val count = matches.groups[1]?.value?.toInt() ?: throw InvalidInput()
                    val cube = Cube.valueOf(matches.groups[2]?.value ?: throw InvalidInput())
                    this.set(cube, count)
                }
            }

            return Showing(cubes)
        }
    }
}
