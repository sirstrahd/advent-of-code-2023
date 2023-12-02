package day02

import utils.println
import utils.readInput
import java.util.*
import kotlin.math.max

enum class Color {
    red, green, blue
}

fun main() {

    fun part1(games: List<Game>, redCubes: Long, greenCubes: Long, blueCubes: Long): Long {
        return games.sumOf {
            val possible = it.hands.all {
                it.values.all {
                    when (it.color) {
                        Color.red -> {
                            it.number <= redCubes
                        }

                        Color.blue -> {
                            it.number <= blueCubes
                        }

                        else -> {
                            it.number <= greenCubes
                        }
                    }
                }
            }
            if (possible) {
                it.id
            } else {
                0
            }
        }
    }

    fun part2(games: List<Game>): Long {
        return games.sumOf {
            var maxRed = 0L
            var maxBlue = 0L
            var maxGreen = 0L
            it.hands.forEach {
                it.values.forEach {
                    when (it.color) {
                        Color.red -> {
                            maxRed = max(maxRed, it.number)
                        }

                        Color.blue -> {
                            maxBlue = max(maxBlue, it.number)
                        }

                        else -> {
                            maxGreen = max(maxGreen, it.number)
                        }
                    }
                }
            }
            maxRed * maxBlue * maxGreen
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val testOutput = part1(parseGames(testInput), 12, 13, 14)
    check(testOutput == 8L) { "expected 8 got $testOutput" }
    val input = readInput("Day02")
    part1(parseGames(input), 12, 13, 14).println()
    val testOutputPart2 = part2(parseGames(testInput))
    check(testOutputPart2 == 2286L) { "expected 2286 got $testOutput" }
    part2(parseGames(input)).println()
}

fun parseGames(testInput: List<String>): List<Game> {
    return testInput.map { line ->
        val parts1 = line.split(":")
        val gameNumber = parts1[0].filter { it.isDigit() }.toLong()
        val result = Game(gameNumber)
        parts1[1].split(";").forEach {
            val hand = Hand()
            it.split(",").forEach {
                val amount = it.filter { it.isDigit() }.toLong()
                if (it.contains(Color.red.toString())) {
                    hand.values.add(ColorAmount(amount, Color.red))
                } else if (it.contains(Color.blue.toString())) {
                    hand.values.add(ColorAmount(amount, Color.blue))
                } else {
                    hand.values.add(ColorAmount(amount, Color.green))
                }
            }
            result.hands.add(hand)
        }
        result
    }
}

class Game(
        val id: Long,
        val hands: MutableList<Hand> = LinkedList()
)

class Hand(
        val values: MutableList<ColorAmount> = LinkedList()
)

class ColorAmount(
        val number: Long,
        val color: Color
)