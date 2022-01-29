package advent.day5

import advent.Base

data class Line(val point1: Pair<Int, Int>, val point2: Pair<Int, Int>) {
    val equation by lazy { getParametersForEquation() }

    fun getXinterval() = if (point1.x < point2.x) {
        Pair(point1.x, point2.x)
    } else {
        Pair(point2.x, point1.x)
    }

    fun getYinterval() = if (point1.y < point2.y) {
        Pair(point1.y, point2.y)
    } else {
        Pair(point2.y, point1.y)
    }

    fun isVertical(): Boolean {
        return equation.first == 0
    }

    fun isHorizontal(): Boolean {
        return equation.second == 0
    }

    fun isSameAs(other: Line) = equation == other.equation

    fun containsPoint(point: Pair<Int, Int>): Boolean {
        if (equation.first * point.x + equation.second * point.y + equation.third != 0) return false

        if ((point.x >= getXinterval().first && point.x <= getXinterval().second) &&
            (point.y >= getYinterval().first && point.y <= getYinterval().second)
        ) {
            return true
        }
        return false
    }

    // return triplet of generic line equation: first * x + second * y + third = 0
    // dX * X + dY * Y + c = 0
    fun getParametersForEquation(): Triple<Int, Int, Int> {
        val u = point2 - point1
        if (u.x == 0) return Triple(1, 0, -point1.x)
//        x = point1.x + u.x * t  -> t = (x - point1.x) / u.x
//        put t into second equation
//        y = point1.y + u.y * t
//        y = point1.y + u.y * (x / u.x - point1.x / u.x)
//        y = point1.y + (u.y  / u.x) * x  - (u.y * point1.x) / u.x
//        - (u.y  / u.x) * x + 1 * y  - point1.y + (u.y * point1.x) / u.x  == 0
        return Triple(
            -(u.y / u.x),
            1,
            -point1.y + (u.y * point1.x) / u.x
        )
    }

    fun getOverlappingEdges(other: Line): Set<Pair<Int, Int>> {
        if (!isSameAs(other)) return emptySet()

        val edges = mutableListOf(point1, point2)
        if (!edges.contains(other.point1)) edges.add(other.point1)
        if (!edges.contains(other.point2)) edges.add(other.point2)
        val finalEdges = edges.filter { containsPoint(it) && other.containsPoint(it) }
        if (finalEdges.size != 2 && finalEdges.size != 1 && finalEdges.isNotEmpty())
            throw Exception("Wrong number of edges: ${finalEdges.size}")
        return finalEdges.toSet()
    }

    fun getOverlappingPoints(edges: Set<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        if (edges.size == 1) return edges
        val points = mutableSetOf<Pair<Int, Int>>()
        if (edges.first().x != edges.last().x) {
            val e1 = if (edges.first().x < edges.last().x) edges.first().x else edges.last().x
            val e2 = if (edges.first().x < edges.last().x) edges.last().x else edges.first().x
            for (x in (e1..e2)) {
                val y = (-equation.dX * x - equation.c) / equation.dY
                points.add(Pair(x, y))
            }
        } else {
            val e1 = if (edges.first().y < edges.last().y) edges.first().y else edges.last().y
            val e2 = if (edges.first().y < edges.last().y) edges.last().y else edges.first().y
            for (y in (e1..e2)) {
                val x = (-equation.dY * y - equation.c) / equation.dX
                points.add(Pair(x, y))
            }
        }

        return points
    }

    fun getIntersectionPoint(other: Line): Pair<Int, Int>? {
        var e1 = equation.makeXpositive()
        var e2 = other.equation
        fun getDeltaY(
            e1: Triple<Int, Int, Int>,
            e2: Triple<Int, Int, Int>
        ) = ((-e2.dX * e1.dY + e2.dY) / e1.dX)

        if ((isHorizontal() && other.isHorizontal()) || (isVertical() && other.isVertical())) return null

        val dY = if (e1.dX == 0 || getDeltaY(e1, e2) == 0) {
            val tmp = e1
            e1 = e2.makeXpositive()
            e2 = tmp
            getDeltaY(e1, e2)
        } else {
            getDeltaY(e1, e2)
        }
        if (dY == 0) return null

        val c = -e2.c + e1.c * e2.dX / e1.dX
        val y = c / dY
        val x = (-e1.dY * y - e1.c) / e1.dX
        return Pair(x, y)
    }

    fun getAllPoints(other: Line): Set<Pair<Int, Int>> {
        val edges = getOverlappingEdges(other)
        if (edges.isNotEmpty()) {
            return getOverlappingPoints(edges)
        }

        val intersection = getIntersectionPoint(other)
        if (intersection != null && containsPoint(intersection) && other.containsPoint(intersection)) {
            return setOf(intersection)
        }
        return emptySet()
    }

    fun print() {
        println(equation)
    }
}


class Fifth : Base() {
    val lines = mutableListOf<Line>()

    override fun parseAndStore(lines: List<String>) {
        for (line in lines) {
            val regex = "^([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)".toRegex()
            val results = regex.find(line)

            assert(results!!.groups.size == 5)
            this.lines.add(
                Line(
                    Pair(results.groupValues[1].toInt(), results.groupValues[2].toInt()),
                    Pair(results.groupValues[3].toInt(), results.groupValues[4].toInt())
                )
            )

        }
    }


    fun compute(allLines: Boolean): Int {
        val points = mutableSetOf<Pair<Int, Int>>()

        val exampleLines = if (allLines) {
            lines
        } else {
            lines.filter { it.isVertical() || it.isHorizontal() }.toMutableList()
        }

        while (exampleLines.isNotEmpty()) {
            val line = exampleLines.removeFirst()
            exampleLines.forEach { other ->

                line.getAllPoints(other).forEach {
                    if (!points.contains(it)) points.add(it)
                }
            }
        }

        return points.size
    }

}

val Pair<Int, Int>.x: Int get() = first
val Pair<Int, Int>.y: Int get() = second

val Triple<Int, Int, Int>.dX: Int get() = first
val Triple<Int, Int, Int>.dY: Int get() = second
val Triple<Int, Int, Int>.c: Int get() = third

fun Triple<Int, Int, Int>.makeXpositive(): Triple<Int, Int, Int> {
    if (first < 0) {
        return Triple(
            first * -1,
            second * -1,
            third * -1
        )
    } else {
        return this
    }
}

infix operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
    Pair(first + other.first, second + other.second)

infix operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) =
    Pair(first - other.first, second - other.second)
