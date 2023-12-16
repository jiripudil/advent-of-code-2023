fun main() {
    fun part1(input: List<String>): Int {
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

        val energized = mutableSetOf<Triple<Int, Int, BeamDirection>>()
        beam(Pair(0, 0), BeamDirection.RIGHT, energized)

        val distinctEnergizedTiles = energized.map { Pair(it.first, it.second) }.toSet()
        return distinctEnergizedTiles.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

enum class BeamDirection { LEFT, RIGHT, UP, DOWN }
