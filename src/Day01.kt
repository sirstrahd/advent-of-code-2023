package day1

import utils.println
import utils.readInput

const val zero = "zero"
const val one = "one"
const val two = "two"
const val three = "three"
const val four = "four"
const val five = "five"
const val six = "six"
const val seven = "seven"
const val eight = "eight"
const val nine = "nine"

const val zeroReplacement = "zero0zero"
const val oneReplacement = "one1one"
const val twoReplacement = "two2two"
const val threeReplacement = "three3three"
const val fourReplacement = "four4four"
const val fiveReplacement = "five5five"
const val sixReplacement = "six6six"
const val sevenReplacement = "seven7seven"
const val eightReplacement = "eigth8eight"
const val nineReplacement = "nine9nine"

fun main() {

    fun String.replaceLast(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
        val index = lastIndexOf(oldValue, ignoreCase = ignoreCase)
        return if (index < 0) this else this.replaceRange(index, index + oldValue.length, newValue)
    }

    fun String.replaceFirstAndLast(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
        return this.replaceFirst(oldValue, newValue, ignoreCase).replaceLast(oldValue, newValue, ignoreCase)
    }

    fun part1(input: List<String>): Int {
        return input.fold(0) {acc, line ->
            val digits = line.filter { c -> c.isDigit()}
            acc + StringBuilder().append(digits.first()).append(digits.last()).toString().toInt()}
    }

    fun part2(input: List<String>): Int {
        return input.map{x -> x.replaceFirstAndLast(zero,zeroReplacement).replaceFirstAndLast(one,oneReplacement).replaceFirstAndLast(two,twoReplacement).replaceFirstAndLast(three,threeReplacement)
                .replaceFirstAndLast(four,fourReplacement).replaceFirstAndLast(five,fiveReplacement)
                .replaceFirstAndLast(six,sixReplacement).replaceFirstAndLast(seven,sevenReplacement).replaceFirstAndLast(eight,eightReplacement)
                .replaceFirstAndLast(nine,nineReplacement)}
                .fold(0) { acc, line ->
            val digits = line.filter { c -> c.isDigit()}
            acc + StringBuilder().append(digits.first()).append(digits.last()).toString().toInt()}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val input = readInput("Day01")
    part1(input).println()


    val test2Input = readInput("Day01_pt2_test")
    val result = part2(test2Input)
    result.println()
    check(result == 281)
    part2(input).println()
}
