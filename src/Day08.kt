package day08

import utils.println
import utils.readInput
import java.util.*

fun main() {
    fun getMap(lines: List<String>): Maps {
        val instructions = lines[0].map {
            Instruction.valueOf("$it")
        }
        val map: MutableMap<String, Pair<String, String>> = HashMap()
        for (i in 2..<lines.size) {
            val regex = Regex("""(.+)\s=\s\((.+),\s(.+)\)""")
            val matchResult = regex.find(lines[i])!!
            val (key, leftValue, rightValue) = matchResult.destructured
            map.put(key, Pair(leftValue, rightValue))
        }
        return Maps(instructions, map)
    }

    fun part1(lines: List<String>): Long {
        val map = getMap(lines)
        var amountMoved = 0L
        var currentPosition = "AAA"
        while(currentPosition != "ZZZ") {
            for(instruction in map.instructions) {
                val currentToDo = map.map[currentPosition]
                currentPosition = if (instruction == Instruction.L) {
                    currentToDo!!.first
                } else {
                    currentToDo!!.second
                }
                amountMoved++
            }
        }
        return amountMoved
    }

    fun part2(lines: List<String>): Long {
        val map = getMap(lines)
        var amountMoved = 0L
        val currentPositions = map.map.keys.filter {
            it.endsWith("A")
        }.toMutableList().toTypedArray()
        val array = ArrayList<Long>()
        for(i in currentPositions.indices) {
            array.add(-1L)
        }
        while(array.any {it -> it == -1L}) {
            for (i in currentPositions.indices) {
                if (array[i] == -1L && currentPositions[i].endsWith("Z")) {
                    array[i] = amountMoved
                }
            }
            for(instruction in map.instructions) {
                for (i in currentPositions.indices) {
                    val currentToDo = map.map[currentPositions[i]]
                    if (instruction == Instruction.L) {
                        currentPositions[i] = currentToDo!!.first
                    } else {
                        currentPositions[i] = currentToDo!!.second
                    }
                }
                amountMoved++
            }
        }
        return findLCMOfListOfNumbers(array)
    }

    var testInput = readInput("Day08_test1")
    var testOutput = part1(testInput)
    check(testOutput ==2L) { "expected 2 got $testOutput" }
    testInput = readInput("Day08_test2")
    testOutput = part1(testInput)
    check(testOutput ==6L) { "expected 6 got $testOutput" }
    val input = readInput("Day08")
    var output = part1(input)
    output.println()
    testInput = readInput("Day08_test_part2")
    testOutput = part2(testInput)
    check(testOutput ==6L) { "expected 6 got $testOutput" }
    output = part2(input)
    output.println()
}

fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}


enum class Instruction {
    L, R
}
class Maps(val instructions: List<Instruction>, val map: Map<String, Pair<String, String>>)
