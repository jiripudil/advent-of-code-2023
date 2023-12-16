fun main() {
    fun compute(input: List<String>): Pair<Int, Int> {
        val width = input[0].length
        val height = input.size
        val grid = buildMap {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    set(Pair(x, y), char)
                }
            }
        }

        fun beam(start: Pair<Int, Int>, initialDirection: BeamDirection, energized: MutableSet<Triple<Int, Int, BeamDirection>>) {
            var position = start
            var direction = initialDirection

            while (position.first in 0..<width && position.second in 0..<height) {
                val tile = grid[position] ?: throw InvalidInput()

                val key = Triple(position.first, position.second, direction)
                if (energized.contains(key)) {
                    break
                }

                energized += key

                when (tile) {
                    '/' -> {
                        when (direction) {
                            BeamDirection.UP -> {
                                position = Pair(position.first + 1, position.second)
                                direction = BeamDirection.RIGHT
                            }
                            BeamDirection.DOWN -> {
                                position = Pair(position.first - 1, position.second)
                                direction = BeamDirection.LEFT
                            }
                            BeamDirection.LEFT -> {
                                position = Pair(position.first, position.second + 1)
                                direction = BeamDirection.DOWN
                            }
                            BeamDirection.RIGHT -> {
                                position = Pair(position.first, position.second - 1)
                                direction = BeamDirection.UP
                            }
                        }
                    }
                    '\\' -> {
                        when (direction) {
                            BeamDirection.UP -> {
                                position = Pair(position.first - 1, position.second)
                                direction = BeamDirection.LEFT
                            }
                            BeamDirection.DOWN -> {
                                position = Pair(position.first + 1, position.second)
                                direction = BeamDirection.RIGHT
                            }
                            BeamDirection.LEFT -> {
                                position = Pair(position.first, position.second - 1)
                                direction = BeamDirection.UP
                            }
                            BeamDirection.RIGHT -> {
                                position = Pair(position.first, position.second + 1)
                                direction = BeamDirection.DOWN
                            }
                        }
                    }
                    '|' -> {
                        position = when (direction) {
                            BeamDirection.UP -> Pair(position.first, position.second - 1)
                            BeamDirection.DOWN -> Pair(position.first, position.second + 1)
                            BeamDirection.LEFT, BeamDirection.RIGHT -> {
                                beam(Pair(position.first, position.second - 1), BeamDirection.UP, energized)
                                beam(Pair(position.first, position.second + 1), BeamDirection.DOWN, energized)
                                break
                            }
                        }
                    }
                    '-' -> {
                        when (direction) {
                            BeamDirection.UP, BeamDirection.DOWN -> {
                                beam(Pair(position.first - 1, position.second), BeamDirection.LEFT, energized)
                                beam(Pair(position.first + 1, position.second), BeamDirection.RIGHT, energized)
                                break
                            }
                            BeamDirection.LEFT -> {
                                position = Pair(position.first - 1, position.second)
                            }
                            BeamDirection.RIGHT -> {
                                position = Pair(position.first + 1, position.second)
                            }
                        }
                    }
                    else -> {
                        position = when (direction) {
                            BeamDirection.UP -> Pair(position.first, position.second - 1)
                            BeamDirection.DOWN -> Pair(position.first, position.second + 1)
                            BeamDirection.LEFT -> Pair(position.first - 1, position.second)
                            BeamDirection.RIGHT -> Pair(position.first + 1, position.second)
                        }
                    }
                }
            }
        }

        fun countEnergized(startingPosition: Pair<Int, Int>, startingDirection: BeamDirection): Int {
            val energized = mutableSetOf<Triple<Int, Int, BeamDirection>>()
            beam(startingPosition, startingDirection, energized)
            return energized.map { Pair(it.first, it.second) }.toSet().size
        }

        val part1 = countEnergized(Pair(0, 0), BeamDirection.RIGHT)

        val configurations = buildList {
            for (y in 0..<height) {
                add(Triple(0, y, BeamDirection.RIGHT))
                add(Triple(width - 1, y, BeamDirection.LEFT))
            }

            for (x in 0..<width) {
                add(Triple(x, 0, BeamDirection.DOWN))
                add(Triple(x, height - 1, BeamDirection.UP))
            }
        }

        val results = configurations.map { Pair(it, countEnergized(Pair(it.first, it.second), it.third)) }
        val part2 = results.maxOf { it.second }

        return Pair(part1, part2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    val (part1Test, part2Test) = compute(testInput)
    check(part1Test == 46)
    check(part2Test == 51)

    val input = readInput("Day16")
    val (part1, part2) = compute(input)
    part1.println()
    part2.println()
}

enum class BeamDirection { LEFT, RIGHT, UP, DOWN }
