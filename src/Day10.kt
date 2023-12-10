fun main() {
    fun part1(input: List<String>): Int {
        var startingPipe: Pipe? = null
        val grid = buildMap {
            input.forEachIndexed { y, line ->
                line.forEachIndexed inner@{ x, char ->
                    if (char == '.') {
                        return@inner
                    }

                    val coordinates = Coordinates(x, y)
                    val pipe = Pipe(coordinates, char)
                    this.set(coordinates, pipe)

                    if (char == 'S') {
                        startingPipe = pipe
                    }
                }
            }
        }

        val visited = mutableSetOf<Coordinates>()
        fun findLoop(pipe: Pipe, previous: Pipe, path: List<Pipe>): List<Pipe>? {
            if (visited.contains(pipe.coordinates)) {
                return path + pipe
            }

            val connections = pipe.connections
                .mapNotNull { grid[it] }
                .filter { it != previous } // do not loop back immediately
                .filter { it.connections.contains(pipe.coordinates) } // only those that actually connect back

            if (connections.isEmpty()) {
                return null
            }

            visited.add(pipe.coordinates)

            connections.forEach {
                val loop = findLoop(it, pipe, path + pipe)
                if (loop != null) {
                    return loop
                }
            }

            return null
        }

        val loop = findLoop(startingPipe!!, startingPipe!!, emptyList()) ?: throw InvalidInput()
        return loop.size.floorDiv(2)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    //check(part1(testInput) == 8)

    val input = readInput("Day10")
    part1(input).println()
    //part2(input).println()
}

data class Coordinates(val x: Int, val y: Int) {
    fun west(): Coordinates = Coordinates(x - 1, y)
    fun east(): Coordinates = Coordinates(x + 1, y)
    fun north(): Coordinates = Coordinates(x, y - 1)
    fun south(): Coordinates = Coordinates(x, y + 1)
}

data class Pipe(val coordinates: Coordinates, val value: Char) {
    val connections: List<Coordinates> get() {
        return when (value) {
            '-' -> listOf(coordinates.east(), coordinates.west())
            '|' -> listOf(coordinates.north(), coordinates.south())
            'F' -> listOf(coordinates.east(), coordinates.south())
            '7' -> listOf(coordinates.west(), coordinates.south())
            'L' -> listOf(coordinates.east(), coordinates.north())
            'J' -> listOf(coordinates.west(), coordinates.north())
            'S' -> listOf(coordinates.east(), coordinates.west(), coordinates.north(), coordinates.south())
            else -> emptyList()
        }
    }
}
