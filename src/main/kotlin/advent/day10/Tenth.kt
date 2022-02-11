package advent.day10

import advent.Base
import java.math.BigInteger

class Tenth : Base() {
    val data = mutableListOf<String>()
    override fun parseAndStore(lines: List<String>) {
        data.addAll(lines)
    }

    fun processLine(line: String): List<String> {
        val openings = mutableListOf<String>()

        line.chunked(1).forEach {
            if (it.isOpening()) {
                openings.add(it)
            } else {
                if (it.isClosureOf(openings.last())) {
                    openings.removeLast()
                } else {
                    return listOf(it)
                }
            }
        }

        return if (openings.isEmpty()) openings else openings.reversed().map { it.flipBracket() }
    }

    fun computeScore(): Int {
        return data.map { processLine(it) }
            .filter { it.size == 1 }
            .map { it.first() }
            .fold(0) { acc, s ->
                acc + s.getScore()
            }
    }

    fun getScoreOnIncompleteLines(): BigInteger {
        return data.map { processLine(it) }
            .filter { it.size > 1 }
            .map { it.getScore() }
            .sorted()
            .let {
                check(it.size.mod(2) == 1) { "Size of output list has to be odd" }
                it[it.size.floorDiv(2)]
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

fun String.getSecondScore(): BigInteger {
    return when (this) {
        "}" -> 3.toBigInteger()
        "]" -> 2.toBigInteger()
        ")" -> 1.toBigInteger()
        ">" -> 4.toBigInteger()
        else -> throw Exception("Wrong character to second score: $this")
    }
}

fun List<String>.getScore(): BigInteger = this.fold(BigInteger.ZERO) { acc, item ->
    acc * 5.toBigInteger() + item.getSecondScore()
}

fun String.isOpening() = listOf("[", "{", "(", "<").contains(this)
fun String.isClosureOf(opening: String): Boolean {
    return opening.flipBracket() == this
}

fun String.flipBracket(): String {
    return when (this) {
        "{" -> "}"
        "[" -> "]"
        "(" -> ")"
        "<" -> ">"
        else -> throw Exception("Wrong bracket:$this")
    }
}