package day10

import utils.readInput
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

enum class Direction {
    TOP, BOT, LEFT, RIGHT
}
class Coordinates(val i: Int, val j: Int) {
    override fun equals(other: Any?)
            = (other is Coordinates)
            && i == other.i
            && j == other.j

    override fun hashCode(): Int {
        var result = i
        result = 31 * result + j
        return result
    }
    // ...
}
class Position(var char: Char, var steps: Int)

val pipeMap = HashMap<Char, List<Direction>>()
fun buildPipeMap(lines: List<String>): Array<Array<Position>> {
    val emptyPosition = Position('.', Int.MAX_VALUE)
    val map: Array<Array<Position>> = Array(lines.size) { Array<Position>(lines[0].length) { emptyPosition } }
    for (i in lines.indices) {
        val line = lines[i]
        for (j in line.indices) {
            val char = line[j]
            val distance = if (char == 'S') 0 else Int.MAX_VALUE
            map[i][j] = Position(char, distance)
        }
    }
    return map
}
fun processPosition(otherTile: Position, thisTile: Position, otherDirection: Direction) {
    val otherPipeExits = pipeMap[otherTile.char]
    if (otherTile.steps != Int.MAX_VALUE && (otherTile.char == 'S' || otherPipeExits != null && otherPipeExits.toList()
            .contains(otherDirection))
    ) {
        thisTile.steps = min(thisTile.steps, otherTile.steps + 1)
    }
}



fun processTile(map: Array<Array<Position>>, i: Int, j: Int): Boolean {
    val thisTile = map[i][j]
    val thisTileOriginalSteps = map[i][j].steps
    val pipeExits = pipeMap[thisTile.char]
    pipeExits?.toList()?.forEach {
        when(it) {
            Direction.BOT -> {
                if (i < map.size - 1) {
                    val otherTile = map[i+1][j]
                    processPosition(otherTile, thisTile, Direction.TOP)
                }
            }
            Direction.TOP -> {
                if (i > 0) {
                    val otherTile = map[i-1][j]
                    processPosition(otherTile, thisTile, Direction.BOT)
                }
            }
            Direction.LEFT -> {
                if (j > 0) {
                    val otherTile = map[i][j-1]
                    processPosition(otherTile, thisTile, Direction.RIGHT)
                }
            }
            Direction.RIGHT -> {
                if (j < map[0].size - 1) {
                    val otherTile = map[i][j+1]
                    processPosition(otherTile, thisTile, Direction.LEFT)
                }
            }
        }
    }
    return (thisTile.steps != thisTileOriginalSteps)
}

fun fillDistances(map: Array<Array<Position>>): Array<Array<Position>> {
    var changed = true
    while(changed) {
        changed = false
        for (i in map.indices) {
            val line = map[i]
            for (j in line.indices) {
                changed = processTile(map, i, j) || changed
            }
        }
    }
    return map
}

fun findMax(map: Array<Array<Position>>): Int {
    var maxValue = 0
    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j].steps != Int.MAX_VALUE) {
                maxValue = max(maxValue, map[i][j].steps)
            }
        }
    }
    return maxValue
}

fun part1(lines: List<String>): Int {
    val map = buildPipeMap(lines)
    fillDistances(map)
    return findMax(map)
}
fun part2(lines: List<String>): Int {
    val map = buildPipeMap(lines)
    fillDistances(map)
    val path = getPath(map)
    val area = Math.abs((0..<path.size).fold(0) {acc, i ->
        val current = path[i]
        val next = if (i+1 == path.size) {path[0]} else {path[i+1]}
        acc + (current.j * next.i) - (current.i * next.j)
    })/2 // Gauss
    return area - (path.size/2) + 1 // Pick theorem
}

fun findS(map: Array<Array<Position>>): Coordinates? {
    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j].char =='S') {
                return Coordinates(i,j)
            }
        }
    }
    return null
}

fun findNext(map: Array<Array<Position>>, list: LinkedList<Coordinates>, expectedLength: Int): Coordinates? {
    val currentElement = list.last()
    val thisInfo = map[currentElement.i][currentElement.j]
    return if (currentElement.i > 0 && map[currentElement.i-1][currentElement.j].steps == expectedLength && !list.contains(Coordinates(currentElement.i-1, currentElement.j))&&pipeMap[thisInfo.char]!!.toList().contains(Direction.TOP)&&pipeMap[ map[currentElement.i-1][currentElement.j].char]!!.toList().contains(Direction.BOT)) {
        Coordinates(currentElement.i-1, currentElement.j)
    } else if (currentElement.i < map.size-1 && map[currentElement.i+1][currentElement.j].steps == expectedLength && !list.contains(Coordinates(currentElement.i+1, currentElement.j))&&pipeMap[thisInfo.char]!!.toList().contains(Direction.BOT)&&pipeMap[ map[currentElement.i+1][currentElement.j].char]!!.toList().contains(Direction.TOP)) {
        Coordinates(currentElement.i+1, currentElement.j)
    } else if (currentElement.j > 0 && map[currentElement.i][currentElement.j-1].steps == expectedLength && !list.contains(Coordinates(currentElement.i, currentElement.j-1))&&pipeMap[thisInfo.char]!!.toList().contains(Direction.LEFT)&&pipeMap[ map[currentElement.i][currentElement.j-1].char]!!.toList().contains(Direction.RIGHT)) {
        Coordinates(currentElement.i, currentElement.j-1)
    } else if (currentElement.j < map[0].size-1 && map[currentElement.i][currentElement.j+1].steps == expectedLength && !list.contains(Coordinates(currentElement.i, currentElement.j+1))&&pipeMap[thisInfo.char]!!.toList().contains(Direction.RIGHT)&&pipeMap[ map[currentElement.i][currentElement.j+1].char]!!.toList().contains(Direction.LEFT)) {
        Coordinates(currentElement.i, currentElement.j+1)
    } else {
        null
    }
}

fun getPath(map: Array<Array<Position>>): LinkedList<Coordinates> {
    val list = LinkedList<Coordinates>()
    var current = findS(map)
    var currentLength = 0
    while(current != null) {
        list.add(current)
        current = findNext(map, list, ++currentLength)
    }
    --currentLength
    current = findNext(map, list, --currentLength)
    while(current != null) {
        list.add(current)
        current = findNext(map, list, --currentLength)
    }
    return list
}



fun main() {
    fillPipeMap()
    val input = readInput("Day10")
    var testOutput = part1(readInput("Day10_test"))
    check(testOutput ==8) { "part1 expected 8 got $testOutput" }
    println("part 1: ${part1(input)}")
    println("part 2: test 1")
    testOutput = part2(readInput("Day10_test_part2_1"))
    check(testOutput ==4) { "part2 expected 4 got $testOutput" }
    println("part 2: test 2")
    testOutput = part2(readInput("Day10_test_part2_2"))
    check(testOutput ==4) { "part2 expected 4 got $testOutput" }
    println("part 2: test 3")
    testOutput = part2(readInput("Day10_test_part2_3"))
    check(testOutput ==8) { "part2 expected 8 got $testOutput" }
    println("part 2: test 4")
    testOutput = part2(readInput("Day10_test_part2_4"))
    check(testOutput ==10) { "part2 expected 10 got $testOutput" }
    println("part 2: ${part2(input)}")
}



private fun fillPipeMap() {
    pipeMap['|'] = listOf(Direction.BOT, Direction.TOP)
    pipeMap['-'] = listOf(Direction.LEFT, Direction.RIGHT)
    pipeMap['F'] = listOf(Direction.BOT, Direction.RIGHT)
    pipeMap['L'] = listOf(Direction.TOP, Direction.RIGHT)
    pipeMap['7'] = listOf(Direction.BOT, Direction.LEFT)
    pipeMap['J'] = listOf(Direction.TOP, Direction.LEFT)
    pipeMap['S'] = listOf(Direction.TOP, Direction.LEFT,Direction.BOT, Direction.RIGHT)
}