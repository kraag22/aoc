package advent.day9

import advent.Base
import java.util.*

class Ninth : Base() {
    var firstPoint: Point? = null

    override fun parseAndStore(lines: List<String>) {
        var lastList: LinkedList<Point>? = null

        lines.forEach { line ->
            val list = LinkedList<Point>()
            line.chunked(1).forEachIndexed { idx, char ->
                val point = Point(char.toInt())
                if (list.isNotEmpty()) {
                    point.left = list.last
                    list.last.right = point
                }
                lastList?.let {
                    val upPoint = it[idx]
                    upPoint.bottom = point
                    point.up = upPoint
                }
                list.addLast(point)

                if (firstPoint == null) {
                    firstPoint = point
                }
            }
            lastList = list
        }
    }

    fun computeRiskLevel(firstPoint: Point) = findLowestPoints(firstPoint).sumOf { it.height + 1 }

    fun computeBasins(firstPoint: Point): Int {
        return findLowestPoints(firstPoint)
            .map { it.getBasinSize() }
            .sorted()
            .takeLast(3)
            .fold(1) { acc, i ->
                acc * i
            }
    }

    fun findLowestPoints(firstPoint: Point): List<Point> {
        val lowestPoints = mutableListOf<Point>()
        goThrough(firstPoint) { point ->
            if (point.isLowest()) {
                lowestPoints.add(point)
            }
        }

        return lowestPoints
    }

    fun goThrough(firstPoint: Point, callback: (Point) -> Unit) {
        var column = firstPoint
        while (true) {
            var row = column
            while (true) {
                callback(row)
                row = row.right ?: break
            }
            column = column.bottom ?: break
        }
    }

    fun printer(start: Point, queue: List<Point>) {
        var first: Point? = firstPoint
        repeat(70) {
            first = first?.bottom
        }
        print(first!!, start, queue)
        repeat(3) {
            println()
        }
    }

    fun print(firstPoint: Point, start: Point, queue: List<Point>) {
        var p = firstPoint
        while (true) {
            printRow(p, start, queue)
            p = p.bottom ?: break
        }
    }

    fun printRow(firstPoint: Point, start: Point, queue: List<Point>) {
        var p = firstPoint
        while (true) {
            printPoint(p, start, queue)
            p = p.right ?: break
        }
        println()
    }

    fun printPoint(point: Point, start: Point, queue: List<Point>) {
        when (point) {
            start -> print("X")
            in queue -> print("*")
            else -> print(point.height)
        }
    }

    fun getPoint(firstPoint: Point, x: Int, y: Int): Point {
        var p = firstPoint
        repeat(x) {
            p = p.right!!
        }

        repeat(y) {
            p = p.bottom!!
        }
        return p
    }
}

class Point(
    val height: Int,
    var left: Point? = null,
    var right: Point? = null,
    var up: Point? = null,
    var bottom: Point? = null
) {
    fun isLowest(): Boolean {
        val neighbours = listOfNotNull(left, right, up, bottom)
        val higherNeighbours = neighbours.fold(0) { acc, point ->
            acc + if (point.height <= height) 1 else 0
        }
        return higherNeighbours == 0
    }

    fun getBasinSize(printer: ((Point, List<Point>) -> Unit)? = null): Int {
        val queue = mutableListOf(this)
        var idx = 0
        var lastHeight = 0

        while (idx < queue.size) {
            val point = queue[idx]

            if (printer != null && lastHeight != point.height) {
                lastHeight = point.height
                println("height:$lastHeight")
                printer(this, queue)
            }

            queue.addAll(
                listOfNotNull(point.left, point.right, point.up, point.bottom)
                    .filter { it.height > point.height && it.height < 9 }
                    .filter { !queue.contains(it) }
            )
            idx++
        }
        return queue.size
    }

    fun getCoordinates(): Pair<Int, Int> {
        var p = this
        var dX = 1
        var dY = 1
        while (p.left != null) {
            p = p.left ?: break
            dX++
        }

        p = this
        while (p.up != null) {
            p = p.up ?: break
            dY++
        }
        return Pair(dX, dY)
    }
}

