fun main() {
    fun part1(seedsLine: String, converter: (seeds: LongRange) -> List<LongRange>): Long {
        val seeds = seedsLine.split(' ').drop(1).map { it.toLong() }
        return seeds.flatMap { converter(LongRange(it, it)) }.minOf { it.first }
    }

    fun part2(seedsLine: String, converter: (seeds: LongRange) -> List<LongRange>): Long {
        val seedRanges = seedsLine
            .split(' ')
            .drop(1)
            .chunked(2)
            .map { (a, b) ->
                val start = a.toLong()
                val length = b.toLong()
                start ..< (start + length)
            }

        return seedRanges
            .flatMap(converter)
            .minOf { it.first }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val testConverter = buildConverter(testInput.drop(2))
    check(part1(testInput.first(), testConverter) == 35L)
    check(part2(testInput.first(), testConverter) == 46L)

    val input = readInput("Day05")
    val converter = buildConverter(input.drop(2))
    part1(input.first(), converter).println()
    part2(input.first(), converter).println()
}

fun buildConverter(input: List<String>): (seeds: LongRange) -> List<LongRange> {
    class Mapping(val range: LongRange, val diff: Long) {
        fun map(n: Long) = n + diff
    }

    class ConversionMap(input: List<Mapping>) {
        private val mappings: List<Mapping> = input.sortedBy { it.range.first }

        fun convert(range: LongRange): List<LongRange> {
            if (mappings.isEmpty()) {
                return listOf(range)
            }

            var result = mutableListOf<LongRange>()

            var n = range.first
            var mappingIndex = 0

            do {
                val mapping = mappings[mappingIndex]

                if (n > mapping.range.last) {
                    mappingIndex++
                    continue
                }

                if (n < mapping.range.first) {
                    if (range.last < mapping.range.first) {
                        result += n..range.last
                        return result
                    }

                    result += n..<mapping.range.first
                    n = mapping.range.first
                    continue
                }

                if (range.last < mapping.range.last) {
                    result += mapping.map(n)..mapping.map(range.last)
                    return result
                }

                result += mapping.map(n)..mapping.map(mapping.range.last)
                n = mapping.range.last + 1
                mappingIndex++

            } while (n <= range.last && mappingIndex <= mappings.lastIndex)

            if (n <= range.last) {
                result += n..range.last
            }

            return result
        }

        fun convert(ranges: List<LongRange>): List<LongRange> {
            return ranges.flatMap(::convert)
        }
    }

    var mapType: String? = null
    val intermediate = mutableListOf<Mapping>()
    val conversions = mutableMapOf<String, ConversionMap>()

    input.forEach { line ->
        if (line == "") {
            if (mapType != null) {
                conversions[mapType!!] = ConversionMap(intermediate)
                intermediate.clear()
            }

            return@forEach
        }

        if (line.endsWith("map:")) {
            mapType = line.split(' ').first()
            return@forEach
        }

        val (destStart, srcStart, length) = line.split(' ', limit = 3).map { it.toLong() }
        val srcRange = LongRange(srcStart, srcStart + length - 1)
        intermediate.add(Mapping(srcRange, destStart - srcStart))
    }

    conversions[mapType!!] = ConversionMap(intermediate)

    return { seeds: LongRange ->
        val soil = conversions.getValue("seed-to-soil").convert(listOf(seeds))
        val fertilizer = conversions.getValue("soil-to-fertilizer").convert(soil)
        val water = conversions.getValue("fertilizer-to-water").convert(fertilizer)
        val light = conversions.getValue("water-to-light").convert(water)
        val temperature = conversions.getValue("light-to-temperature").convert(light)
        val humidity = conversions.getValue("temperature-to-humidity").convert(temperature)
        conversions.getValue("humidity-to-location").convert(humidity)
    }
}
