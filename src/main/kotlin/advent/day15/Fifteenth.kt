package advent.day15

import advent.Base
import kotlin.math.absoluteValue

class Fifteenth(private val gridSize: Int = 10) : Base() {
    var gridValues = Array(gridSize) {
        Array(gridSize) {
            -1
        }
    }

    var gridPath = Array(gridSize) {
        Array(gridSize) {
            -1
        }
    }

    init {
        gridPath[0][0] = 0
    }

    override fun parseAndStore(lines: List<String>) {
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                gridValues[x][y] = value.digitToInt()
            }
        }
    }

    fun compute() {
        var toProcess = mutableListOf(Pair(0, 0))

        while (toProcess.isNotEmpty()) {
            val point = toProcess.removeFirst()
            gridPath.fillPathForPoint(gridValues, point)
            val neighbours = gridPath.getNotVisitedNeighbours(point)
            toProcess.addAll(neighbours)
            toProcess =
                toProcess.distinct()
                    .sortedBy { gridValues[it.first][it.second] + (gridPath.getMinNeighboursPath(it) ?: 0) }
                    .toMutableList()
        }
    }

    fun print(grid: Array<Array<Int>>) {
        for (y in grid.first().indices) {
            for (x in grid.first().indices) {
                printPointValue(grid[x][y])
            }
            println()
        }
        println()
    }

    private fun printPointValue(it: Int) {
        print(
            if (it >= 0) {
                when (it.length) {
                    1 -> "   $it"
                    2 -> "  $it"
                    3 -> " $it"
                    else -> it
                }
            } else {
                " X "
            }
        )
    }

    fun getMinPathValue(): Int {
        return gridPath[gridSize - 1][gridSize - 1]
    }
}

fun <T> Array<Array<T>>.getNeighbours(point: Pair<Int, Int>): List<Pair<Int, Int>> {
    val points = mutableListOf<Pair<Int, Int>>()
    val x = point.first
    val y = point.second

    if (x > 0) points.add(Pair(x - 1, y))
    if (y > 0) points.add(Pair(x, y - 1))
    if (y < this.size - 1) points.add(Pair(x, y + 1))
    if (x < this.size - 1) points.add(Pair(x + 1, y))

    return points
}

fun Array<Array<Int>>.getNotVisitedNeighbours(point: Pair<Int, Int>) =
    getNeighbours(point).filter { this[it.first][it.second] < 0 }

fun Array<Array<Int>>.getVisitedNeighbours(point: Pair<Int, Int>) =
    getNeighbours(point).filter { this[it.first][it.second] >= 0 }

fun Array<Array<Int>>.fillPathForPoint(gridValues: Array<Array<Int>>, point: Pair<Int, Int>) {
    if (point.first != 0 && point.second != 0) {
        require(this[point.first][point.second] == -1) { "Only unvisited point can be filled: $point" }
    }

    getMinNeighboursPath(point)?.let {
        this[point.first][point.second] = it + gridValues[point.first][point.second]
    }
}


fun Array<Array<Int>>.getMinNeighboursPath(point: Pair<Int, Int>?): Int? {
    if (point == null) return null

    return getVisitedNeighbours(point).let { points ->
        if (points.isNotEmpty()) {
            points.minByOrNull {
                this[it.first][it.second]
            }
        } else {
            null
        }
    }?.let {
        this[it.first][it.second]
    }
}

val Int.length
    get() = when (this) {
        0 -> 1
        else -> kotlin.math.log10(toDouble().absoluteValue).toInt() + 1
    }