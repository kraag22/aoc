package advent.day14

import advent.Base
import java.math.BigInteger
import java.math.RoundingMode

class Fourteenth : Base() {
    var polymerBase: String? = null
    val rules = mutableMapOf<String, String>()

    override fun parseAndStore(lines: List<String>) {
        polymerBase = lines.first()
        lines.drop(2).forEach {
            val (base, expand) = it.split("->")
            rules[base.trim()] = expand.trim()
        }
    }

    fun getScoreAfter(steps: Int): BigInteger {
        var couples = polymerBase!!.toCouples()
        repeat(steps) {
            couples = doStep(couples)
        }

        return getScore(couples)
    }

    fun doStep(couples: MutableMap<String, BigInteger>): MutableMap<String, BigInteger> {
        val newCouples = mutableMapOf<String, BigInteger>()
        couples.keys.forEach { couple ->
            val count = couples.getOrDefault(couple, BigInteger.ZERO)
            if (rules.containsKey(couple)) {
                val first = couple[0] + rules[couple]!!
                val second = rules[couple]!! + couple[1]
                newCouples.addOrSet(first, count)
                newCouples.addOrSet(second, count)
            } else {
                newCouples.addOrSet(couple, count)
            }
        }
        return newCouples
    }

    fun getScore(couples: MutableMap<String, BigInteger>): BigInteger {
        val letters = mutableMapOf<String, BigInteger>()
        couples.forEach { (key, count) ->
            letters.addOrSet(key[0].toString(), count)
            letters.addOrSet(key[1].toString(), count)
        }

        letters.keys.forEach {
            val count = letters[it] ?: throw Exception("Missing key: $it")
            letters[it] = count.toBigDecimal().divide(2.toBigDecimal(), RoundingMode.CEILING).toBigInteger()
        }

        val sorted = letters.map { (key, value) ->
            Pair(key, value)
        }.sortedBy { it.second }

        return sorted.last().second - sorted.first().second
    }
}

fun MutableMap<String, BigInteger>.addOrSet(key: String, count: BigInteger) {
    val previous = this[key]
    if (previous != null) {
        this[key] = count + previous
    } else {
        this[key] = count
    }
}

fun String.toCouples(): MutableMap<String, BigInteger> {
    val res = mutableMapOf<String, BigInteger>()
    windowed(2, 1).forEach {
        res.addOrSet(it, BigInteger.ONE)
    }
    return res
}
