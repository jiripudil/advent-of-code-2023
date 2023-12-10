fun main() {
    fun part1(input: List<String>): Int {
        val directions = input.first().chunked(1).map { DesertPath.Direction.valueOf(it) }
        val nodes = input.drop(2).map {
            val match = Regex("(\\w+) = \\((\\w+), (\\w+)\\)").find(it) ?: throw InvalidInput()
            Pair(match.groupValues[1], Pair(match.groupValues[2], match.groupValues[3]))
        }.toMap()

        var steps = 0
        var position = "AAA"

        while (true) {
            for (direction in directions) {
                val node = nodes.getValue(position)
                position = when (direction) {
                    DesertPath.Direction.L -> node.first
                    DesertPath.Direction.R -> node.second
                }

                steps++

                if (position == "ZZZ") {
                    return steps
                }
            }
        }
    }

    fun part2(input: List<String>): Long {
        val directions = input.first().chunked(1).map { DesertPath.Direction.valueOf(it) }
        val nodes = input.drop(2).map {
            val match = Regex("(\\w+) = \\((\\w+), (\\w+)\\)").find(it) ?: throw InvalidInput()
            Pair(match.groupValues[1], Pair(match.groupValues[2], match.groupValues[3]))
        }.toMap()

        val paths = nodes.keys
            .filter { it.endsWith("A") }
            .map { DesertPath(it, nodes, directions) }
            .toMutableSet()

        val loopingPositions = mutableListOf<Int>()

        while (paths.isNotEmpty()) {
            paths.forEach { it.advance() }

            val loops = paths.filter { it.loopingPosition != null }
            paths.removeAll(loops)
            loopingPositions.addAll(loops.map { it.loopingPosition!! })
        }

        return loopingPositions.map { it.toLong() }.reduce(::lcm)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val test2Input = readInput("Day08_test2")
    check(part1(testInput) == 2)
    check(part2(test2Input) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

class DesertPath(
    startingPosition: String,
    private val nodes: Map<String, Pair<String, String>>,
    private val directions: List<Direction>
) {
    var currentPosition = Pair(startingPosition, 0)
        private set

    var loopingPosition: Int? = null
        private set

    private val visited = mutableListOf<Pair<String, Int>>()

    fun advance() {
        val index = visited.size % directions.size

        visited.add(currentPosition)

        val currentNode = nodes.getValue(currentPosition.first)
        val direction = directions[index]
        currentPosition = when (direction) {
            Direction.L -> Pair(currentNode.first, index + 1)
            Direction.R -> Pair(currentNode.second, index + 1)
        }

        val currentIndex = visited.indexOf(currentPosition)
        if (currentPosition.first.endsWith("Z") && currentIndex != -1) {
            loopingPosition = currentIndex
        }
    }

    enum class Direction { L, R }
}

fun gcd(a: Long, b: Long): Long {
    var i = a
    var j = b

    while (j > 0) {
        val tmp = j
        j = i % j
        i = tmp
    }

    return i
}

fun lcm(a: Long, b: Long): Long = a * (b / gcd(a, b))
