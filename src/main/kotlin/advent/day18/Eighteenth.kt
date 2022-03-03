package advent.day18

import advent.Base

class Eighteenth : Base() {
    fun parse(input: String, previous: Snailfish? = null): Snailfish {
        val snailfish = Snailfish(previous)
        if (!input.contains(',')) {
            snailfish.value = input.toLong()
            return snailfish
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
                        snailfish.pair = Pair(parse(left, snailfish), parse(right, snailfish))
                        return snailfish
                    }
                }
                else -> {}
            }
        }

        throw Exception("Unexpected end of parsing")
    }
}

class Snailfish {
    var value: Long? = null
    var pair: Pair<Snailfish, Snailfish>? = null
    var parent: Snailfish? = null

    constructor(_previous: Snailfish?) {
        value = null
        pair = null
        parent = _previous
    }

    constructor(_value: Long, _previous: Snailfish?) {
        value = _value
        pair = null
        parent = _previous
    }

    constructor(_pair: Pair<Snailfish, Snailfish>, _previous: Snailfish?) {
        value = null
        pair = _pair
        parent = _previous
    }

    fun setValue(_v: Long) {
        value = _v
        pair = null
    }

    private fun increaseValue(increase: Long) {
        value = value!! + increase
    }

    fun getLevel(baseLevel: Int? = null): Int {
        val level = baseLevel ?: 0
        return if (isSingle()) {
            level
        } else {
            val p = pair
            check(p != null) { "Missing pair for nonSingle snailfish" }
            maxOf(p.first.getLevel(level + 1), p.second.getLevel(level + 1))
        }
    }

    fun explode(): Boolean {
        val toExplode = getExploding() ?: return false
        val increaseLeft =
            toExplode.pair?.first?.value ?: throw Exception("Value cannot be empty for fish: ${toExplode.print()}")
        val increaseRight =
            toExplode.pair?.second?.value ?: throw Exception("Value cannot be empty for fish: ${toExplode.print()}")

        val leftNeighbour = toExplode.parent?.getNeighbour(goDown = true, goLeft = true, toExplode)
        val rightNeighbour = toExplode.parent?.getNeighbour(goDown = true, goLeft = false, toExplode)

        leftNeighbour?.increaseValue(increaseLeft)
        rightNeighbour?.increaseValue(increaseRight)

        toExplode.setValue(0)
        return true
    }

    fun getExploding(baseLevel: Int? = null): Snailfish? {
        val level = baseLevel ?: 1
        check(level < 6) { "Level cannot be that high: $level fish: ${this.print()}" }
        return if (isSingle()) {
            null
        } else {
            val p = pair
            check(p != null) { "Missing pair for nonSingle snailfish" }
            if (level == 5) {
                this
            } else {
                p.first.getExploding(level + 1) ?: p.second.getExploding(level + 1)
            }
        }
    }

    fun getNeighbour(goDown: Boolean, goLeft: Boolean, previous: Snailfish): Snailfish? {
        return if (isSingle()) {
            this
        } else {
            val res = computeNeighbours(goDown, goLeft, previous)
            if (res == null) {
                if (parent == null) {
                    null
                } else {
                    parent!!.getNeighbour(true, goLeft, this)
                }
            } else {
                res
            }
        }
    }

    private fun computeNeighbours(goDown: Boolean, goLeft: Boolean, previous: Snailfish): Snailfish? {
        return if (goDown) {
            if (goLeft) {
                go(pair!!.first, goLeft, previous)
            } else {
                go(pair!!.second, goLeft, previous)
            }
        } else {
            if (goLeft) {
                go(pair!!.second, goLeft, previous) ?: go(pair!!.first, goLeft, previous)
            } else {
                go(pair!!.first, goLeft, previous) ?: go(pair!!.second, goLeft, previous)
            }
        }
    }

    private fun go(to: Snailfish, goLeft: Boolean, previous: Snailfish): Snailfish? {
        return if (to == previous) {
            null
        } else {
            to.getNeighbour(false, goLeft, this)
        }
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