package day06

import utils.println
import utils.readInput
import java.util.*

fun main() {
    fun part1(races: List<Race>): Long {
        return races.map {
            var amount = 0L
            for(i in 1..<it.time) {
                val distance = ((it.time-i)*i)
                if (distance > it.distance) {
                    amount++
                }
            }
            amount
        }.fold(1L) { acc, x ->
            acc * x
        }
    }

    fun part2(race: Race): Long {
        var amount = 0L
        for(i in 1..<race.time) {
            val distance = ((race.time-i)*i)
            if (distance > race.distance) {
                amount++
            }
        }
        return amount
    }
    val testInput = readInput("Day06_test")
    val racesTest = parseInput(testInput)
    val testOutput = part1(racesTest)
    check(testOutput ==288L) { "expected 288 got $testOutput" }
    val input = readInput("Day06")
    val races = parseInput(input)
    val output = part1(races)
    output.println()

    val raceTestPart2 = parseInputPart2(testInput)
    val testOutputPart2 = part2(raceTestPart2)
    check(testOutputPart2 ==71503L) { "expected 71503 got $testOutputPart2" }
    val racePart2 = parseInputPart2(input)
    val outputPart2 = part2(racePart2)
    outputPart2.println()

}



fun parseInput(lines: List<String>): List<Race> {
    val result = LinkedList<Race>()
    val times = lines[0].split(":")[1].trim().split("\\s+".toRegex()).map{it.toLong()}
    val distances = lines[1].split(":")[1].trim().split("\\s+".toRegex()).map{it.toLong()}
    times.indices.forEach {
        result.add(Race(times[it], distances[it]))
    }
    return result
}

fun parseInputPart2(lines: List<String>): Race {
    val time = lines[0].split(":")[1].trim().split("\\s+".toRegex()).fold("") {acc, x -> acc + x}.toLong()
    val distance = lines[1].split(":")[1].trim().split("\\s+".toRegex()).fold("") {acc, x -> acc + x}.toLong()
    return Race(time, distance)
}

class Race(val time: Long, val distance: Long)

