package advent.day18

import advent.Base

class Eighteenth : Base() {
    fun parse(input: String): Snailfish {
        if (!input.contains(',')) {
            return Snailfish(input.toLong(), null)
        }

        var bracketDepth = 0
        input.forEachIndexed { idx, c ->
            when (c) {
                '[' -> bracketDepth++
                ']' -> bracketDepth--
                ',' -> {
                    if (bracketDepth == 1) {
                        val left = input.drop(1).take(idx - 1)
                        val right = input.drop(idx + 1).dropLast(1)
                        return Snailfish(Pair(parse(left), parse(right)), null)
                    }
                }
                else -> {

                }
            }
        }

        throw Exception("shouldnt end like this")
    }
}

class Snailfish {
    var value: Long? = null
    var pair: Pair<Snailfish, Snailfish>? = null
    var previous: Snailfish? = null

    constructor(_value: Long, _previous: Snailfish?) {
        value = _value
        pair = null
        previous = _previous
    }

    constructor(_pair: Pair<Snailfish, Snailfish>, _previous: Snailfish?) {
        value = null
        pair = _pair
        previous = _previous
    }

    fun print(): String {
        return if (isSingle()) {
            value.toString()
        } else {
            "[" + pair?.first?.print() + ',' + pair?.second?.print() + ']'
        }
    }

    fun isSingle() = value != null
}