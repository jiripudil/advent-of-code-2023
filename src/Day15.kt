fun main() {
    fun hash(s: String) = s.fold(0) { acc, char -> ((acc + char.code) * 17) % 256 }

    fun part1(input: String): Int {
        val segments = input.split(',')
        return segments.sumOf { hash(it) }
    }

    fun part2(input: String): Int {
        val boxes = List(256) { LensBox() }

        val segments = input.split(',')
        segments.forEach {
            val match = Regex("^(\\w+)([=-])(\\d+)?$").find(it) ?: throw InvalidInput()
            val label = match.groups.get(1)?.value ?: throw InvalidInput()
            val operation = match.groups.get(2)?.value ?: throw InvalidInput()
            val operand = match.groups.get(3)?.value?.toInt()

            val labelHash = hash(label)
            val box = boxes[hash(label)]

            if (operation == "=" && operand != null) {
                box.replaceOrAdd(label, operand)
            } else if (operation == "-") {
                box.remove(label)
            }
        }

        var sum = 0
        boxes.forEachIndexed { index, box ->
            var boxSum = 0
            box.slots.forEachIndexed { slotIndex, slot ->
                boxSum += (index + 1) * (slotIndex + 1) * slot.second
            }

            sum += boxSum
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test").first()
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15").first()
    part1(input).println()
    part2(input).println()
}

typealias LensSlot = Pair<String, Int>
class LensBox(val slots: MutableList<LensSlot> = emptyList<LensSlot>().toMutableList()) {
    fun replaceOrAdd(label: String, focalLength: Int) {
        val slot = LensSlot(label, focalLength)
        val index = slots.indexOfFirst { it.first == label }
        if (index == -1) {
            slots.add(slot)
        } else {
            slots[index] = slot
        }
    }

    fun remove(label: String) {
        val index = slots.indexOfFirst { it.first == label }
        if (index != -1) {
            slots.removeAt(index)
        }
    }
}
