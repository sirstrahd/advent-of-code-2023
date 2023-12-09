package day07

import utils.println
import utils.readInput
import java.util.*

fun main() {
    fun part1(games: List<HandAndBet>): Long {
        val sortedGames = games.sortedWith(HandComparatorPart1())
        return sortedGames.mapIndexed { i, it ->
            it.bet * (i + 1)
        }.sum()
    }

    fun part2(games: List<HandAndBet>): Long {
        val sortedGames = games.sortedWith(HandComparatorPart2())
        return sortedGames.mapIndexed { i, it ->
            it.bet * (i + 1)
        }.sum()
    }

    val testInput = readInput("Day07_test")
    val handsTest = parseInputPart1(testInput)
    val testOutput = part1(handsTest)
    check(testOutput ==6440L) { "expected 6440L got $testOutput" }
    val input = readInput("Day07")
    val hands = parseInputPart1(input)
    val output = part1(hands)
    output.println()
    val handsTestPart2 = parseInputPart2(testInput)
    val testOutputPart2 = part2(handsTestPart2)
    check(testOutputPart2 ==5905L) { "expected 5905L got $testOutputPart2" }
    val handsPart2 = parseInputPart2(input)
    val outputPart2 = part2(handsPart2)
    outputPart2.println()
}


class HandComparatorPart2 : Comparator<HandAndBet> {
    override fun compare(o1: HandAndBet, o2: HandAndBet): Int {
        return if (o1.type < o2.type) {
            -1
        } else if (o1.type > o2.type) {
            1
        } else {
            compareStrings(o1.string, o2.string)
        }
    }

    private fun compareStrings(o1: String, o2: String): Int {
        for (i in 0..4) {
            if (o1[i] != o2[i]) {
                return value(o1[i]) - value(o2[i])
            }
        }
        return 0
    }

    private fun value(char: Char): Int {
        return if (char.isDigit()) {
            char.digitToInt(10)
        } else if (char == 'A') {
            13
        } else if (char == 'K') {
            12
        } else if (char == 'Q') {
            11
        } else if (char == 'J') {
            1
        } else if (char == 'T') {
            10
        } else {
            throw Error("WTF2")
        }
    }
}

class HandComparatorPart1 : Comparator<HandAndBet> {
    override fun compare(o1: HandAndBet, o2: HandAndBet): Int {
        return if (o1.type < o2.type) {
            -1
        } else if (o1.type > o2.type) {
            1
        } else {
            compareStrings(o1.string, o2.string)
        }
    }

    private fun compareStrings(o1: String, o2: String): Int {
        for (i in 0..4) {
            if (o1[i] != o2[i]) {
                return value(o1[i]) - value(o2[i])
            }
        }
        return 0
    }

    private fun value(char: Char): Int {
        return if (char.isDigit()) {
            char.digitToInt(10)
        } else if (char == 'A') {
            14
        } else if (char == 'K') {
            13
        } else if (char == 'Q') {
            12
        } else if (char == 'J') {
            11
        } else if (char == 'T') {
            10
        } else {
            throw Error("WTF2")
        }
    }
}

enum class HandType {
    HIGH, ONE_PAIR, TWO_PAIR,THREE,FULL_HOUSE,FOUR,FIVE,
}

fun parseInputPart1(lines: List<String>): List<HandAndBet> {
    return lines.map{
        val parts = it.split(" ")
        HandAndBet(getTypePart1(parts[0]), parts[0], parts[1].toLong())
    }
}
fun parseInputPart2(lines: List<String>): List<HandAndBet> {
    return lines.map{
        val parts = it.split(" ")
        HandAndBet(getTypePart2(parts[0]), parts[0], parts[1].toLong())
    }
}

class HandAndBet(val type: HandType, val string: String, val bet: Long)

private fun getByJokers(topNumber: Int, jokers: Int): HandType {
    val total = topNumber+jokers
    when (total) {
        1 -> {
            return HandType.HIGH
        }
        2 -> {
            return HandType.ONE_PAIR
        }
        3 -> {
            return HandType.THREE
        }
        4 -> {
            return HandType.FOUR
        }
        5 -> {
            return HandType.FIVE
        }
        else -> {
            throw Error("WTF3")
        }
    }
}
private fun getTypePart2(hand: String): HandType {
    val occurrencesMap = mutableMapOf<Char, Int>()
    for (c in hand) {
        occurrencesMap.putIfAbsent(c, 0)
        occurrencesMap[c] = occurrencesMap[c]!! + 1
    }
    val jokers = occurrencesMap.getOrDefault('J', 0)
    if (jokers == 5) {
        return HandType.FIVE
    }
    occurrencesMap.remove('J')
    val sortedList = occurrencesMap.values.sortedDescending()
    if (sortedList[0] == 1) {
        return getByJokers(sortedList[0], jokers)
    } else if (sortedList[0] == 2 && (sortedList.size == 1 || sortedList[1] == 1)) {
        return getByJokers(sortedList[0], jokers)
    } else if (sortedList[0] == 2 && (sortedList[1] == 2)) {
        return if (jokers == 0) {
            HandType.TWO_PAIR
        } else {
            HandType.FULL_HOUSE
        }
    } else if (sortedList[0] == 3 && (sortedList.size == 1 || sortedList[1] == 1)) {
        return getByJokers(sortedList[0], jokers)
    } else if (sortedList[0] == 4) {
        return getByJokers(sortedList[0], jokers)
    } else if (sortedList[0] == 3 && sortedList[1] == 2) {
        return HandType.FULL_HOUSE
    } else if (sortedList[0] == 5) {
        return HandType.FIVE
    } else {
        throw Exception("WTF")
    }
}

private fun getTypePart1(hand: String): HandType {
    val occurrencesMap = mutableMapOf<Char, Int>()
    for (c in hand) {
        occurrencesMap.putIfAbsent(c, 0)
        occurrencesMap[c] = occurrencesMap[c]!! + 1
    }
    val sortedList = occurrencesMap.values.sortedDescending()
    if (sortedList[0] == 1) {
        return HandType.HIGH
    } else if (sortedList[0] == 2 && sortedList[1] == 1) {
        return HandType.ONE_PAIR
    } else if (sortedList[0] == 2 && sortedList[1] == 2) {
        return HandType.TWO_PAIR
    } else if (sortedList[0] == 3 && sortedList[1] == 1) {
        return HandType.THREE
    } else if (sortedList[0] == 4) {
        return HandType.FOUR
    } else if (sortedList[0] == 3 && sortedList[1] == 2) {
        return HandType.FULL_HOUSE
    } else if (sortedList[0] == 5) {
        return HandType.FIVE
    } else {
        throw Exception("WTF")
    }
}


