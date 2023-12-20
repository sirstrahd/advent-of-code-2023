package day11

import utils.readInput
import java.util.*
import kotlin.math.abs

class Position(var y: Int, var x: Int)

fun expand(galaxies: List<Position>, originalSizeY:Int, originalSizeX: Int, amountToExpand: Int) {
    var sizeX = originalSizeX
    var sizeY = originalSizeY
    var i = 1
    while(i < sizeY) {
        if (!galaxies.any{it.y == i}) {
            expandY(galaxies,i, amountToExpand-1)
            sizeY+=amountToExpand-1
            i+=amountToExpand-1
        }
        i++
    }
    i = 1
    while(i < sizeX) {
        if (!galaxies.any{it.x == i}) {
            expandX(galaxies,i, amountToExpand-1)
            sizeX+=amountToExpand-1
            i+=amountToExpand-1
        }
        i++
    }
}

fun expandX(galaxies: List<Position>, i: Int, amountToExpand: Int) {
    for(galaxy in galaxies) {
        if (galaxy.x > i) {
            galaxy.x+=amountToExpand
        }
    }
}

fun expandY(galaxies: List<Position>, i: Int, amountToExpand: Int) {
    for(galaxy in galaxies) {
        if (galaxy.y > i) {
            galaxy.y+=amountToExpand
        }
    }
}

fun part1(lines: List<String>): Long {
    val galaxies = getGalaxies(lines)
    expand(galaxies, lines.size, lines[0].length, 2)
    return sumDistances(galaxies)
}

fun part2(lines: List<String>): Long {
    val galaxies = getGalaxies(lines)
    expand(galaxies, lines.size, lines[0].length, 1000000)
    return sumDistances(galaxies)
}

fun part2test(lines: List<String>): Long {
    val galaxies = getGalaxies(lines)
    expand(galaxies, lines.size, lines[0].length, 10)
    return sumDistances(galaxies)
}

fun distance(pos1: Position, pos2: Position): Long {
    return abs(pos1.x-pos2.x).toLong() + abs(pos1.y-pos2.y).toLong()
}
fun sumDistances(galaxies: List<Position>): Long {
    var sum = 0L
    for(i in galaxies.indices) {
        for(j in i+1..<galaxies.size) {
            val distance = distance(galaxies[i], galaxies[j])
            sum+=distance
        }
    }
    return sum
}

fun getGalaxies(lines: List<String>): List<Position> {
    val galaxies = LinkedList<Position>()
    for (i in lines.indices) {
        val line = lines[i]
        for (j in line.indices) {
            if (lines[i][j] == '#') {
                galaxies.add(Position(i, j))
            }
        }
    }
    return galaxies
}


fun main() {
    val input = readInput("Day11")
    val testInput = readInput("Day11_test")
    var testOutput = part1(testInput)
    check(testOutput ==374L) { "wrong value, got $testOutput" }
    println("part 1: ${part1(input)}")
    testOutput = part2test(testInput)
    check(testOutput ==1030L) { "wrong value, got $testOutput" }
    println("part 2: ${part2(input)}") // 707506178138 too high
}




