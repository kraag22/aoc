package advent.day13

import advent.Base

class Thirteenth : Base() {
    val points = mutableSetOf<Pair<Int, Int>>()
    var folds = mutableListOf<Pair<String, Int>>()

    override fun parseAndStore(lines: List<String>) {
        lines.forEach { line ->
            if (line.contains(",")) {
                val (x, y) = line.split(",")
                points.add(Pair(x.toInt(), y.toInt()))
            } else if (line.contains("fold along")) {
                val (axis, value) = line.drop(11).split("=")
                folds.add(Pair(axis, value.toInt()))
            }
        }
    }

    fun compute(): Int {
        var processPoints = points
        for (fold in folds) {
            val foldedPoints = mutableSetOf<Pair<Int, Int>>()
            processPoints.forEach { point ->
                val newPoint = foldPoint(fold.first, fold.second, point)
                if (!foldedPoints.contains(newPoint)) {
                    foldedPoints.add(newPoint)
                }
            }
            processPoints = foldedPoints
        }
        print(processPoints, 15)
        return processPoints.size
    }

    fun foldPoint(axis: String, foldBase: Int, point: Pair<Int, Int>): Pair<Int, Int> {
        when (axis) {
            "x" -> {
                if (point.first > foldBase) {
                    val diff = point.first - foldBase
                    return Pair(foldBase - diff, point.second)
                }
                if (point.first == foldBase) throw Exception("null point")
            }
            "y" -> {
                if (point.second > foldBase) {
                    val diff = point.second - foldBase
                    return Pair(point.first, foldBase - diff)
                }
                if (point.second == foldBase) throw Exception("null point")
            }
            else -> throw Exception("Unknown axis: $axis")
        }

        return point
    }

    private fun print(grid: Set<Pair<Int, Int>>, size: Int) {
        (0..size).forEach { y ->
            (0..6 * size).forEach { x ->
                print(if (grid.contains(Pair(x, y))) "#" else " ")
            }
            println()
        }
        println("-------------------")
    }
}