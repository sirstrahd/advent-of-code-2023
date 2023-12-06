package day04

import utils.println
import utils.readInput
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {

    fun expOf2(matchesFound: Int): Long {
        if (matchesFound == 0) {
            return 0;
        }
        return BigInteger.valueOf(2).pow(matchesFound-1).toLong()
    }

    fun play(game: Game): Long {
        var currentWinningIndex = 0
        var matchesFound = 0;
        game.numbersIHave.forEach {
            while(currentWinningIndex < game.winningNumbers.size && it > game.winningNumbers[currentWinningIndex] ) {
                currentWinningIndex++
            }
            if (currentWinningIndex < game.winningNumbers.size && it == game.winningNumbers[currentWinningIndex]) {
                matchesFound++
                currentWinningIndex++
            }
        }
        game.winningCards = matchesFound;
        return expOf2(game.winningCards);
    }

    fun games(lines: List<String>): List<Game> {
        val games = lines.map {
            val parts = it.split(":\\s+|(\\s+\\|\\s+)".toRegex())
            val winningNumbers = parts[1].split("\\s+".toRegex()).map { it.toInt() }.sorted()
            val numbersIHave = parts[2].split("\\s+".toRegex()).map { it.toInt() }.sorted()
            Game(winningNumbers, numbersIHave)
        }
        return games
    }

    fun part1(games: List<Game>): Long {
        return games.fold(0L) { acc, game ->
            acc + play(game)
        }
    }

    fun part2(games: List<Game>): Long {
        var total = 0L;
        for (i in games.indices) {
            val wins = games[i].winningCards;
            if (wins > 0) {
                for (j in 1..wins) {
                    games[i+j].numberOfCards+=games[i].numberOfCards
                }
            }
            total += games[i].numberOfCards
        }
        return total
    }
    var part1Time: Long
    var part2Time: Long
    val total = measureTimeMillis {
        var games: List<Game>;

        // test if implementation meets criteria from the description, like:
        var testInput = readInput("Day04_test")
        var testGames = games(testInput)
        var testOutput = part1(testGames)
        check(testOutput == 13L) { "expected 13 got $testOutput" }
        var input = readInput("Day04")
        part1Time = measureTimeMillis {
        games = games(input)
            part1(games).println()
        }
        testOutput = part2(testGames)
        check(testOutput == 30L) { "expected 30 got $testOutput" }
        part2Time = measureTimeMillis {
            part2(games).println()
        }
    }
    println("Total time $total ms to run solution. Part1: $part1Time Part2: $part2Time")
}

class Game (
    val winningNumbers: List<Int> = LinkedList(),
    val numbersIHave: List<Int> = LinkedList()
) {
    var winningCards = 0;
    var numberOfCards = 1;
}


