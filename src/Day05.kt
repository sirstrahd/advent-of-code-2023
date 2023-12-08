package day05

import utils.println
import utils.readInput
import java.util.*
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {

    fun translate(current: Long, map: List<Triplet>): Long {
        var result = current
        for(triplet in map) {
            if (current >= triplet.startSource && current <= (triplet.startSource + triplet.range)) {
                result = triplet.startTarget + (current - triplet.startSource)
            }
        }
        return result
    }

    fun part1(almanac: Almanac): Long {
        var minLocation = Long.MAX_VALUE
        for(seed in almanac.seeds) {
            var current = seed
            for(map in almanac.maps) {
                current = translate(current, map)
            }
            minLocation = min(minLocation, current)
        }
        return minLocation
    }

    fun translateRanges(input: LinkedList<SeedRange>, translationMap: List<Triplet>): LinkedList<SeedRange> {
        val output = LinkedList<SeedRange>()
        while(input.isNotEmpty()) {
            val currentSeedRange = input.pop()
            var found = false;
            for (translationMapEntry in translationMap) {
                val x = translationMapEntry.startSource
                val r = translationMapEntry.range
                val s = currentSeedRange.start
                val l = currentSeedRange.range
                val t = translationMapEntry.startTarget
                if (isContainedInRange(x,r,s)) {
                    found=true
                    val offset = s - x
                    val targetStartForThis = t + offset
                    if (isContainedInRange(x,r,s+l-1)) {
                        output.add(SeedRange(targetStartForThis, l))
                    } else {
                        val lengthOfOutput = x + r - s
                        output.add(SeedRange(targetStartForThis, lengthOfOutput))
                        input.push(SeedRange(x + r, l - lengthOfOutput))
                    }
                    break
                } else if (isContainedInRange(x,r,s+l-1)) {
                    found=true
                    input.add(SeedRange(s, x-s))
                    output.add(SeedRange(t, s + l - x))
                    break
                } else if (isContainedInRange(s,l,x) && isContainedInRange(s,l,r)) {
                    found=true
                    output.add(SeedRange(t, r))
                    input.add(SeedRange(s, x-s))
                    input.add(SeedRange(x+r, l-r-(x-s)))
                    break
                }
            }
            if (!found) {
                output.add(currentSeedRange)
            }
        }
        return output
    }

    fun getMinFromSeedRanges(seedRanges: List<SeedRange>): Long {
        var minValue = Long.MAX_VALUE
        for(seed in seedRanges) {
            minValue = min(minValue, seed.start)
        }
        return minValue
    }

    fun part2(almanac: Almanac): Long {
        var x = 0
        var seedRanges = LinkedList<SeedRange>()
        while(x < almanac.seeds.size) {
            seedRanges.add(SeedRange(almanac.seeds[x], almanac.seeds[x+1]))
            x+=2
        }
        for(translationMap in almanac.maps) {
            seedRanges = translateRanges(seedRanges, translationMap)

        }
        return getMinFromSeedRanges(seedRanges)
    }
    val testAlmanac: Almanac = toAlmanac(readInput("Day05_test"))
    val testOutput = part1(testAlmanac)
    check(testOutput == 35L) { "expected 35 got $testOutput" }
    val input = readInput("Day05")
    val timePart1 = measureTimeMillis {part1(toAlmanac(input)).println()}
    val timePart2 = measureTimeMillis {part2(toAlmanac(input)).println()}
    println("Time for part1 $timePart1. Time for part2 $timePart2")
}

fun toAlmanac(input: List<String>): Almanac {
    val almanac = Almanac();
    almanac.seeds.addAll(input[0].split(": ")[1].split(" ").map{ it -> it.toLong()})
    var currentMapIdx = 0;
    for (x in 3..<input.size) {
        if (input[x] == "") {
            continue
        }
        if (input[x].contains("map:")) {
            currentMapIdx++
        } else {
            val values = input[x].split(" ").map { it -> it.toLong() }
            if (currentMapIdx >= almanac.maps.size) {
                almanac.maps.add(LinkedList())
            }
            almanac.maps[currentMapIdx].add(Triplet(values[0], values[1], values[2]))
        }
    }
    return almanac
}

fun isContainedInRange(x: Long, r: Long, check: Long): Boolean {
    return (check >= x && check < (x + r))
}

class Triplet(
    val startTarget: Long, val startSource: Long, val range: Long
)
class Almanac (
    val seeds: MutableList<Long> = LinkedList(),
    val maps: MutableList<MutableList<Triplet>> = ArrayList(),

)

class SeedRange (
    val start: Long, val range: Long
)

