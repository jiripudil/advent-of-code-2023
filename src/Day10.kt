fun main() {
    fun compute(input: List<String>): Pair<Int, Int> {
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
        fun findLoop(pipe: Pipe, previous: Pipe, path: List<Coordinates>): List<Coordinates>? {
            if (visited.contains(pipe.coordinates)) {
                return path + pipe.coordinates
            }

            val connections = pipe.connections
                .mapNotNull { grid[pipe.coordinates.connect(it)] }
                .filter { it != previous } // do not loop back immediately
                .filter { it.connections.map { connection -> it.coordinates.connect(connection) }.contains(pipe.coordinates) } // only those that actually connect back

            if (connections.isEmpty()) {
                return null
            }

            visited.add(pipe.coordinates)

            connections.forEach {
                val loop = findLoop(it, pipe, path + pipe.coordinates)
                if (loop != null) {
                    return loop
                }
            }

            return null
        }

        val loop = findLoop(startingPipe!!, startingPipe!!, emptyList()) ?: throw InvalidInput()

        val minX = loop.minOf { it.x }
        val maxX = loop.maxOf { it.x }
        val minY = loop.minOf { it.y }
        val maxY = loop.maxOf { it.y }

        fun listActualConnections(pipe: Pipe): List<PipeConnection> {
            if (pipe.value != 'S') {
                return pipe.connections
            }

            return pipe.connections.filter {
                val next = pipe.coordinates.connect(it)
                grid[next]?.connections?.contains(it.reverse()) ?: false
            }
        }

        class Ray(val origin: Coordinates, val direction: PipeConnection) {
            private val range = when (direction) {
                PipeConnection.North -> (minY..<origin.y).reversed().map { Coordinates(origin.x, it) }
                PipeConnection.South -> ((origin.y + 1)..maxY).map { Coordinates(origin.x, it) }
                PipeConnection.East -> ((origin.x + 1)..maxX).map { Coordinates(it, origin.y) }
                PipeConnection.West -> (minX..<origin.x).reversed().map { Coordinates(it, origin.y) }
            }

            fun countIntersections(): Int {
                var count = 0
                var pendingTurn: PipeConnection? = null

                range.forEach { point ->
                    if (!loop.contains(point)) {
                        return@forEach
                    }

                    val pipe = grid.getValue(point)
                    val connections = listActualConnections(pipe)

                    if (connections.contains(direction) && connections.contains(direction.reverse())) {
                        return@forEach
                    }

                    if (connections.contains(direction.perpendicular()) && connections.contains(direction.perpendicular().reverse())) {
                        count++
                        return@forEach
                    }

                    if (pendingTurn == null && !(connections - direction).contains(direction.reverse())) {
                        pendingTurn = (connections - direction).first()
                        count++

                    } else if (pendingTurn != null && connections.contains(direction.reverse())) {
                        if (pendingTurn == (connections - direction.reverse()).first()) {
                            count++
                        }

                        pendingTurn = null
                    }
                }

                return count
            }
        }

        val enclosedPoints = mutableListOf<Coordinates>()
        for (x in minX .. maxX) {
            for (y in minY .. maxY) {
                val point = Coordinates(x, y)
                if (loop.contains(point)) {
                    continue
                }

                val rays = listOf(
                    Ray(point, PipeConnection.North),
                    Ray(point, PipeConnection.South),
                    Ray(point, PipeConnection.East),
                    Ray(point, PipeConnection.West),
                )

                val isEnclosed = rays.all { ray -> ray.countIntersections() % 2 != 0 }
                if (isEnclosed) {
                    enclosedPoints += point
                }
            }
        }

        return Pair(
            loop.size.floorDiv(2),
            enclosedPoints.size
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val (part1Test, part2Test) = compute(testInput)
    check(part1Test == 8)

    val input = readInput("Day10")
    val (part1, part2) = compute(input)
    part1.println()
    part2.println()
}

data class Coordinates(val x: Int, val y: Int) {
    fun connect(connection: PipeConnection): Coordinates {
        return when (connection) {
            PipeConnection.North -> Coordinates(x, y - 1)
            PipeConnection.South -> Coordinates(x, y + 1)
            PipeConnection.East -> Coordinates(x + 1, y)
            PipeConnection.West -> Coordinates(x - 1, y)
        }
    }
}

enum class PipeConnection {
    North, South, East, West;

    fun reverse(): PipeConnection = when (this) {
        North -> South
        South -> North
        East -> West
        West -> East
    }

    fun perpendicular(): PipeConnection = when (this) {
        North -> East
        East -> South
        South -> West
        West -> North
    }
}

data class Pipe(val coordinates: Coordinates, val value: Char) {
    val connections: List<PipeConnection> get() {
        return when (value) {
            '-' -> listOf(PipeConnection.East, PipeConnection.West)
            '|' -> listOf(PipeConnection.North, PipeConnection.South)
            'F' -> listOf(PipeConnection.East, PipeConnection.South)
            '7' -> listOf(PipeConnection.West, PipeConnection.South)
            'L' -> listOf(PipeConnection.East, PipeConnection.North)
            'J' -> listOf(PipeConnection.West, PipeConnection.North)
            'S' -> listOf(PipeConnection.East, PipeConnection.West, PipeConnection.North, PipeConnection.South)
            else -> emptyList()
        }
    }
}
