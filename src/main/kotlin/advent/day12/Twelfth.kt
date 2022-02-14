package advent.day12

import advent.Base

class Twelfth : Base() {
    val graph = mutableMapOf<String, Point>()
    val computedPaths = mutableListOf<List<String>>()
    var enableOneLittleTwice = false

    override fun parseAndStore(lines: List<String>) {
        lines.forEach {
            val (a, b) = it.split("-")
            val pointA = preparePoint(a)
            val pointB = preparePoint(b)
            val path = Pair(a, b)
            pointA.paths.add(path)
            pointB.paths.add(path)
        }
    }

    fun preparePoint(name: String): Point {
        if (graph[name] == null) {
            graph[name] = Point(name)
            graph[name]
        }

        return graph.getOrElse(name) {
            throw Exception("missing point: $name")
        }
    }

    fun compute() {
        doStep(listOf("start"))
    }

    fun doStep(path: List<String>) {
        val current = path.last()
        if (current == "end") {
            computedPaths.add(path)
            return
        }

        val options = getValidJumps(path)
        if (options.isEmpty()) return

        options.forEach {
            doStep(path + it)
        }

        return
    }

    fun getValidJumps(path: List<String>): List<String> {
        val point = graph[path.last()] ?: throw Exception("Missing point: ${path.last()}")
        require(point.name != "end") { "Cannot get jumps for end point." }
        val visitedLittlePoints = path.filter { it.isLittle() }
        var twiceVisitedLittlePoint = visitedLittlePoints
            .groupBy { it }
            .filter { (_, value) ->
                value.size == 2
            }
            .values.firstOrNull()?.first()

        return point.paths
            .map { it.getOther(point.name) }
            .filterNot {
                if (enableOneLittleTwice && twiceVisitedLittlePoint == null) {
                    false
                } else {
                    visitedLittlePoints.contains(it)
                }
            }
            .filterNot {
                if (enableOneLittleTwice) {
                    val result = it == twiceVisitedLittlePoint
                    if (twiceVisitedLittlePoint == null && visitedLittlePoints.contains(it)) {
                        twiceVisitedLittlePoint = it
                    }
                    result
                } else {
                    false
                }
            }
            .filterNot { it == "start" }
    }

    data class Point(val name: String) {
        val paths = mutableListOf<Pair<String, String>>()

        fun isLittle() = name.isLittle()
    }
}

fun Pair<String, String>.same(other: Pair<String, String>): Boolean =
    (other.first == first && other.second == second) || (other.second == first && other.first == second)

fun Pair<String, String>.contains(other: String): Boolean = first == other || second == other
fun Pair<String, String>.getOther(current: String): String = if (current == first) second else first
fun String.isLittle(): Boolean = get(0).isLowerCase()

