package advent.day6

import advent.Base
import java.math.BigInteger

class Sixth : Base() {
    var fishes = mutableMapOf<Int, Fish>()

    override fun parseAndStore(lines: List<String>) {
        check(lines.size == 1) {
            "Input file has to have only one line"
        }
        repeat(9) {
            fishes[it] = ShellFish(it, BigInteger.ZERO)
        }
        val inputs = lines.first().split(",")
        inputs.map { it.toInt() }.forEach { fishes[it]!!.fish++ }
    }

    fun processDay() {
        val newFish = fishes[0]!!.fish
        fishes.remove(0)
        fishes.values.forEach { it.nextDay() }
        fishes = fishes.values.associateBy { it.day }.toMutableMap()
        fishes[8] = ShellFish(8, newFish)
        fishes[6]!!.fish += newFish
    }

    fun process(days: Int) {
        repeat(days) {
            processDay()
        }
    }

    fun countFish(): BigInteger {
        return fishes.values.fold(BigInteger.ZERO) { acc, f ->
            acc + f.fish
        }
    }
}

interface Fish {
    var day: Int
    var fish: BigInteger
    fun nextDay()
}

data class ShellFish(override var day: Int, override var fish: BigInteger) : Fish {
    override fun nextDay() {
        day--
    }
}


