package advent.day7

import advent.Base
import kotlin.math.abs

class Seventh : Base() {
    var rows = mutableListOf<Int>()

    fun loadFile(resource: String) {
        loadData(readData(resource).first())
    }

    fun loadData(line: String) {
        rows = line.split(',').map { it.toInt() }.toMutableList()
    }

    fun computeFuelForColumnOnePerStep(column: Int): Int {
        var fuel = 0

        rows.forEach {
            fuel += abs(column - it)
        }
        return fuel
    }

    fun computeFuelForColumnWithIncreasingCost(column: Int): Int {
        var fuel = 0

        rows.forEach {
            val steps = abs(column - it)
            fuel += steps * (steps + 1) / 2
        }
        return fuel
    }

    fun bruteForce(increasingFuel: Boolean = false): Int? {
        val fuelCost = mutableListOf<Pair<Int, Int>>()
        val min = rows.minOrNull()
        val max = rows.maxOrNull()

        if (min != null && max != null) {
            for (i in min..max) {
                val fuel = if (increasingFuel) {
                    computeFuelForColumnWithIncreasingCost(i)
                } else {
                    computeFuelForColumnOnePerStep(i)
                }
                fuelCost.add(Pair(i, fuel))
            }
        } else {
            throw Exception("No min: $min or no max: $max found in rows")
        }

        return fuelCost.minOfOrNull { it.second }
    }

}