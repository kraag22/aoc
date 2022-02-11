package advent.day10

import advent.Base

class Tenth : Base() {
    val data = mutableListOf<String>()
    override fun parseAndStore(lines: List<String>) {
        data.addAll(lines)
    }

    fun processLine(line: String): String {
        val openings = mutableListOf<String>()

        line.chunked(1).forEach {
            if (it.isOpening()) {
                openings.add(it)
            } else {
                if (it.isClosureOf(openings.last())) {
                    openings.removeLast()
                } else {
                    return it
                }
            }
        }

        return if (openings.isEmpty()) "o" else "i"
    }

    fun computeScore(): Int {
        return data.map { processLine(it) }
            .filter { it != "o" && it != "i" }
            .fold(0) { acc, closure ->
                acc + closure.getScore()
            }
    }
}

fun String.getScore(): Int {
    return when (this) {
        "}" -> 1197
        "]" -> 57
        ")" -> 3
        ">" -> 25137
        else -> throw Exception("Wrong character to score: $this")
    }
}

fun String.isOpening() = listOf("[", "{", "(", "<").contains(this)
fun String.isClosureOf(opening: String): Boolean {
    return when (this) {
        "}" -> opening == "{"
        "]" -> opening == "["
        ")" -> opening == "("
        ">" -> opening == "<"
        else -> false
    }
}