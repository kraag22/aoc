package advent2022.day1

import advent.Base
import java.math.BigInteger

class First() : Base() {
    val calories = mutableListOf<List<BigInteger>>()

    override fun parseAndStore(lines: List<String>) {
        val buffer = mutableListOf<BigInteger>()
        lines.forEach { line ->
            if (line.isBlank()) {
                calories.add(buffer.toList())
                buffer.removeAll { true }
            } else {
                buffer.add(line.toBigInteger())
            }
        }

        calories.add(buffer.toList())
    }

    fun findMax():BigInteger {
        return calories.map { group ->
            group.sumOf { it }
        }.maxOf { it }
    }

    fun findMaxThree():BigInteger {
        return calories.map { group ->
            group.sumOf { it }
        }.sortedDescending().take(3).sumOf { it }
    }
}
