package day09

import utils.println
import utils.readInput

fun main() {

    fun recursePart1(values: List<Long>): Long {
        return if (values.all { it == 0L }) {
            0L
        } else {
            values.last() + recursePart1(values.subList(1, values.size).mapIndexed { i, it ->  it - values[i] })
        }
    }

    fun recursePart2(values: List<Long>): Long {
        return if (values.all { it == 0L }) {
            0L
        } else {
            values.first() - recursePart2(values.subList(1, values.size).mapIndexed { i, it ->  it - values[i] })
        }
    }

    fun part1(lines: List<String>): Long {
        return lines.map {recursePart1(it.split(" ").map {it.toLong()})}.sum()
    }

    fun part2(lines: List<String>): Long {
        return lines.map {recursePart2(it.split(" ").map {it.toLong()})}.sum()
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")
    var testOutput = part1(testInput)
    check(testOutput ==114L) { "expected 114 got $testOutput" }
    part1(input).println()
    testOutput = part2(testInput)
    check(testOutput ==2L) { "expected 2 got $testOutput" }
    part2(input).println()

}