package day03

import utils.println
import utils.readInput
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    fun obtainAllPositions(lines: List<String>): AllPositions {
        val allPositions = AllPositions()
        for ((linesIndex, line) in lines.withIndex()) {
            var lastStart = -1
            for ((lineIndex, character) in line.withIndex()) {
                if (character.isDigit()) {
                    if (lastStart == -1) {
                        lastStart = lineIndex
                    }
                } else {
                    if (character != '.') {
                        allPositions.characters.add(XY(lineIndex, linesIndex))
                    }
                    if (lastStart != -1) {
                        allPositions.numbers.add(NumberPositions(XY(lastStart, linesIndex), XY(lineIndex - 1, linesIndex)))
                    }
                    lastStart = -1
                }
            }
            if (lastStart != -1) {
                allPositions.numbers.add(NumberPositions(XY(lastStart, linesIndex), XY(line.length - 1, linesIndex)))
            }
        }
        return allPositions
    }

    fun obtainAllPositionsPart2(lines: List<String>): AllPositions {
        val allPositions = AllPositions()
        for ((linesIndex, line) in lines.withIndex()) {
            var lastStart = -1
            for ((lineIndex, character) in line.withIndex()) {
                if (character.isDigit()) {
                    if (lastStart == -1) {
                        lastStart = lineIndex
                    }
                } else {
                    if (character == '*') {
                        allPositions.characters.add(XY(lineIndex, linesIndex))
                    }
                    if (lastStart != -1) {
                        allPositions.numbers.add(NumberPositions(XY(lastStart, linesIndex), XY(lineIndex - 1, linesIndex)))
                    }
                    lastStart = -1
                }
            }
            if (lastStart != -1) {
                allPositions.numbers.add(NumberPositions(XY(lastStart, linesIndex), XY(line.length - 1, linesIndex)))
            }
        }
        return allPositions
    }

    fun isInContact(numPos: XY, charPos: XY): Boolean {
        if (numPos.x == charPos.x || numPos.x == charPos.x - 1 || numPos.x == charPos.x + 1) {
            if (numPos.y == charPos.y || numPos.y == charPos.y - 1 || numPos.y == charPos.y + 1) {
                return true
            }
        }
        return false
    }

    fun isInContactRange(numberPos: NumberPositions, charPos: XY): Boolean {
        var x = numberPos.start.x
        while (x <= numberPos.end.x) {
            if (isInContact(XY(x, numberPos.start.y), charPos)) {
                return true
            }
            x++
        }
        return false
    }

    fun part1(lines: List<String>): Int {
        val allPositions: AllPositions = obtainAllPositions(lines)
        val relevantNumbers = allPositions.numbers.filter { numberPos ->
            allPositions.characters.any { characterPos ->
                isInContactRange(numberPos, characterPos)
            }
        }
        return relevantNumbers.sumOf {
            lines[it.start.y].substring(it.start.x, it.end.x+1).toInt()
        }
    }

    fun part2(lines: List<String>): Int {
        val allPositions: AllPositions = obtainAllPositionsPart2(lines)
        val result = allPositions.characters.map { gearPos ->
            val numbers = allPositions.numbers.filter { numberPos ->
                isInContactRange(numberPos, gearPos)
            }
            if (numbers.size == 2) {
                lines[numbers[0].start.y].substring(numbers[0].start.x, numbers[0].end.x+1).toInt()*lines[numbers[1].start.y].substring(numbers[1].start.x, numbers[1].end.x+1).toInt()
            } else {
                0
            }
        }
        return result.sum()
    }
    val total = measureTimeMillis {
        // test if implementation meets criteria from the description, like:
        val testInput = readInput("Day03_test")
        val testOutput = part1(testInput)
        check(testOutput == 4361) { "expected 4361 got $testOutput" }
        val input = readInput("Day03")
        val timeInMillis1 = measureTimeMillis { part1(input).println() }
        val testOutputPart2 = part2(testInput)
        check(testOutputPart2 == 467835) { "expected 467835 got $testOutputPart2" }
        val timeInMillis2 = measureTimeMillis {
            part2(input).println()
        }
        println("Took $timeInMillis1 ms to run solution for part 1")
        println("Took $timeInMillis2 ms to run solution for part 2")
    }
    println("Total time $total ms to run solution")
}

class AllPositions(
        val numbers: MutableList<NumberPositions> = LinkedList(),
        val characters: MutableList<XY> = LinkedList(),
)

class XY(
        val x: Int,
        val y: Int
)

class NumberPositions(
        val start: XY,
        val end: XY
)
