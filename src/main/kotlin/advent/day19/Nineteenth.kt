package advent.day19

import advent.Base
import kotlin.math.pow
import kotlin.math.sqrt

typealias Point = Triple<Int, Int, Int>

class Nineteenth : Base() {
    val scanners = mutableMapOf<Int, List<Point>>()

    override fun parseAndStore(lines: List<String>) {
        val incompleteScannerData = mutableListOf<Point>()
        var lastScannerNo = -1
        lines
            .filterNot { it.isEmpty() }
            .forEach { line ->
                if (line.contains("scanner")) {
                    if (incompleteScannerData.isNotEmpty()) scanners[lastScannerNo] = incompleteScannerData.toList()
                    lastScannerNo = line.removePrefix("--- scanner").removeSuffix("---").trim().toInt()
                    incompleteScannerData.clear()
                } else {
                    val regex =
                        "^(-?[0-9]+),(-?[0-9]+),(-?[0-9]+)".toRegex()
                    val results = regex.find(line)

                    assert(results!!.groups.size == 4)

                    val numbers = results.groupValues
                        .drop(1)
                        .map { it.toInt() }
                        .toTypedArray()
                    incompleteScannerData.add(Triple(numbers[0], numbers[1], numbers[2]))
                }
            }

        if (incompleteScannerData.isNotEmpty()) scanners[lastScannerNo] = incompleteScannerData.toList()
        incompleteScannerData.clear()
    }

    fun computeDistances(scannerNo: Int): Map<Int, List<Pair<Int, Double>>> {
        val scannerData = scanners[scannerNo]!!

        val distances = mutableMapOf<Int, List<Pair<Int, Double>>>()
        for (i in scannerData.indices) {
            val pointDistances = mutableListOf<Pair<Int, Double>>()
            for (j in scannerData.indices) {
                if (i != j) {
                    pointDistances.add(Pair(j, scannerData[i] distance scannerData[j]))
                }
            }
            distances[i] = pointDistances.toList()
        }
        return distances.toMap()
    }

    fun joinAreasTogether() {
        val baseArea = scanners.keys.first()
        val areas = scanners.keys.drop(1).toMutableSet()
        val joinedAreas = mutableSetOf<Int>()

        while (areas.isNotEmpty()) {
            areas.forEach { toAdd ->
                println(toAdd)
                if (tryToAddScannerArea(baseArea, toAdd)) {
                    joinedAreas.add(toAdd)
                }
            }
            println("Joined areas: $joinedAreas")
            areas.removeAll(joinedAreas)
        }

    }

    fun tryToAddScannerArea(base: Int, toAdd: Int): Boolean {
        val modification = getModificationFor(base, toAdd) ?: return false
        val pointsInBaseCoordinates = scanners.getValue(toAdd).map { it.modify(modification) }
        val allPoints = scanners.getValue(base) + pointsInBaseCoordinates
        scanners[base] = allPoints.distinct()
        return true
    }

    fun getModificationFor(
        scannerA: Int,
        scannerB: Int
    ): Triple<AxisModification, AxisModification, AxisModification>? {
        val points = findSimilarPoints(scannerA, scannerB)

        if (points.size >= 10) {
            return getModificationFor(points)
        }
        return null
    }

    fun findSimilarPoints(scannerA: Int, scannerB: Int): List<Pair<Point, Point>> {
        val distancesA = computeDistances(scannerA)
        val distancesB = computeDistances(scannerB)

        val points = mutableListOf<Pair<Int, Int>>()
        for (i in (0 until distancesA.size)) {
            val similarPoint = findSimilarity(i, distancesA, distancesB)
            if (similarPoint != null) {
                points.add(Pair(i, similarPoint))
            }
        }

        return points.map {
            Pair(scanners.getPoint(scannerA, it.first), scanners.getPoint(scannerB, it.second))
        }
    }

    fun findSimilarity(
        base: Int,
        distances: Map<Int, List<Pair<Int, Double>>>,
        malformedDistances: Map<Int, List<Pair<Int, Double>>>
    ): Int? {
        val similarities = mutableListOf<Int>()
        // Rounding to INT, verify if that is OK
        val baseDistances = distances.getValue(base).map { it.second.toInt() }.toSet()

        for (i in malformedDistances.keys) {
            // Rounding to INT, verify if that is OK
            val currPointDistancies = malformedDistances.getValue(i).map { it.second.toInt() }.toSet()
            if (baseDistances.intersect(currPointDistancies).size > 9) {
                similarities.add(i)
            }
        }
        return when (similarities.size) {
            1 -> similarities.first()
            0 -> null
            else -> throw Exception("Point $base has ${similarities.size} similarities")
        }
    }

    fun getModificationFor(points: List<Pair<Point, Point>>): Triple<AxisModification, AxisModification, AxisModification> {
        val baseX = points.map { it.first.first }
        val baseY = points.map { it.first.second }
        val baseZ = points.map { it.first.third }
        val others = listOf(
            points.map { it.second.first },
            points.map { it.second.second },
            points.map { it.second.third }
        )

        return Triple(
            getAxisModificationWithOrderFor(baseX, others),
            getAxisModificationWithOrderFor(baseY, others),
            getAxisModificationWithOrderFor(baseZ, others)
        )
    }
}

fun getAxisModificationWithOrderFor(base: List<Int>, others: List<List<Int>>): AxisModification {
    var orderNo = 0
    var modification: AxisModification? = null

    for (i in (0..2)) {
        modification = getAxisModificationFor(base, others[i])
        if (modification != null) {
            orderNo = i
            break
        }
    }
    return modification?.copy(order = orderNo) ?: throw Exception("Unable to compute AxisModification")
}

fun getAxisModificationFor(base: List<Int>, other: List<Int>): AxisModification? {
    var result: AxisModification? = null

    ScannerPerspectiveChange.values().forEach { perspective ->
        val shift = perspective.getShift(base.first(), other.first())
        base.forEachIndexed { idx, v ->
            if (shift != perspective.getShift(v, other[idx])) {
                return@forEach
            }
        }
        result = AxisModification(shift, -1, perspective)
    }
    return result
}

fun MutableMap<Int, List<Point>>.getPoint(screenNo: Int, pointIdx: Int) =
    getValue(screenNo)[pointIdx]

fun Point.modify(modification: Triple<AxisModification, AxisModification, AxisModification>): Point {
    val getAxisData = { order: Int ->
        when (order) {
            0 -> first
            1 -> second
            2 -> third
            else -> throw Exception("Unknown axis order: $order")
        }
    }
    val modX = modification.first
    val modY = modification.second
    val modZ = modification.third
    val x = modX.perspective.apply(getAxisData(modX.order), modX.shift)
    val y = modY.perspective.apply(getAxisData(modY.order), modY.shift)
    val z = modZ.perspective.apply(getAxisData(modZ.order), modZ.shift)
    return Point(x, y, z)
}

infix fun Point.distance(to: Point): Double {
    val squareDiff = { a: Int, b: Int -> (b - a).toDouble().pow(2) }
    return sqrt(squareDiff(first, to.first) + squareDiff(second, to.second) + squareDiff(third, to.third))
}

interface Perspective {
    fun getShift(base: Int, other: Int): Int
    fun apply(point: Int, shift: Int): Int
}

enum class ScannerPerspectiveChange : Perspective {
    NEGATIVE {
        override fun getShift(base: Int, other: Int) = base + other
        override fun apply(point: Int, shift: Int) = -point + shift
    },
    POSITIVE {
        override fun getShift(base: Int, other: Int) = base - other
        override fun apply(point: Int, shift: Int) = point + shift
    },
}

data class AxisModification(val shift: Int, val order: Int, val perspective: ScannerPerspectiveChange)