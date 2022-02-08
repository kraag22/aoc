package advent.day8

import advent.Base

class Eigth : Base() {
    var data = mutableListOf<Pair<List<String>, List<String>>>()

    override fun parseAndStore(lines: List<String>) {
        data = lines.map { line ->
            val splits = line.split("|")
            val signals = splits.first().split(" ").filter { it.isNotEmpty() }.map { it.trim() }
            val values = splits.last().split(" ").filter { it.isNotEmpty() }.map { it.trim() }
            Pair(signals, values)
        }.toMutableList()
    }

    fun getUniqueValues(): List<String> {
        return data.map { it.second }.flatMap { values ->
            values.filter { it.length in listOf(2, 4, 3, 7) }
        }
    }

    fun compute(): Int {
        return data.fold(0) { acc, row ->
            acc + translate(row)
        }
    }

    fun translate(row: Pair<List<String>, List<String>>): Int {
        val translateTable = translateSignals(row.first)
        return translateValues(translateTable, row.second)
    }

    fun translateValues(decodedSignals: Map<Set<Char>, Int>, values: List<String>): Int {
        return values
            .map { value ->
                decodedSignals[value.toSet()]
            }
            .joinToString("")
            .toInt()
    }

    fun translateSignals(signals: List<String>): Map<Set<Char>, Int> {
        val sets: List<Set<Char>> = signals.map { it.toSet() }
        val one = sets.first { it.size == 2 }
        val seven = sets.first { it.size == 3 }
        val four = sets.first { it.size == 4 }
        val eight = sets.first { it.size == 7 }

        val six = sets.first { it.size == 6 && !it.containsAll(seven) }

        val d = four.associateBy { ch ->
            sets.filter { it.contains(ch) }.size
        }[7]
        checkNotNull(d)

        val zero = sets.first { it.size == 6 && !it.contains(d) }
        val nine = sets.first { it.size == 6 && it != zero && it != six }
        val three = sets.first { it.size == 5 && it.containsAll(one) }
        val five = sets.first { it.size == 5 && it != three && six.containsAll(it) }
        val two = sets.first { it.size == 5 && it != three && it != five }

        return mapOf(
            one to 1,
            two to 2,
            three to 3,
            four to 4,
            five to 5,
            six to 6,
            seven to 7,
            eight to 8,
            nine to 9,
            zero to 0
        )
    }
}