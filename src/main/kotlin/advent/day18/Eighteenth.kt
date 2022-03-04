package advent.day18

import advent.Base

class Eighteenth : Base() {
    val fishes = mutableListOf<Snailfish>()

    override fun parseAndStore(lines: List<String>) {
        lines.forEach {
            fishes.add(parse(it))
        }
    }

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

    fun process(verbose: Boolean = true): Snailfish {
        var processing = fishes.first()

        fishes.drop(1).forEach { fish ->
            println(processing.print())
            print("+ ")
            println(fish.print())
            processing = add(processing, fish)
            processing.reduce(verbose)

            print("= ")
            println(processing.print())
        }

        return processing
    }

    fun add(a: Snailfish, b: Snailfish): Snailfish {
        val new = Snailfish(null)
        a.parent = new
        b.parent = new
        new.value = null
        new.pair = Pair(a, b)
        return new
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

    fun makeValue(_v: Long) {
        value = _v
        pair = null
    }

    fun makePair(_p: Pair<Snailfish, Snailfish>) {
        value = null
        pair = _p
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

    fun reduce(verbose: Boolean = true) {
        var didExplode = true
        var didSplit = true

        while (didExplode || didSplit) {
            didExplode = explode(verbose)
            if (!didExplode) {
                didSplit = split(verbose)
            }
            if (!verbose) {
                if (didExplode) println("explode ")
                if (didSplit) println("split ")
                println(print())
            }
        }
    }

    fun explode(verbose: Boolean = true): Boolean {
        val toExplode = getExploding() ?: return false
        if (!verbose) print("${toExplode.print()} ")
        val increaseLeft =
            toExplode.pair?.first?.value ?: throw Exception("Value cannot be empty for fish: ${toExplode.print()}")
        val increaseRight =
            toExplode.pair?.second?.value ?: throw Exception("Value cannot be empty for fish: ${toExplode.print()}")

        val leftNeighbour = toExplode.parent?.getNeighbour(goDown = true, goLeft = true, toExplode)
        val rightNeighbour = toExplode.parent?.getNeighbour(goDown = true, goLeft = false, toExplode)

        leftNeighbour?.increaseValue(increaseLeft)
        rightNeighbour?.increaseValue(increaseRight)

        toExplode.makeValue(0)
        return true
    }

    fun getExploding(baseLevel: Int? = null): Snailfish? {
        val level = baseLevel ?: 1
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

    fun split(verbose: Boolean = true): Boolean {
        val toBeSplit = getSplitting() ?: return false
        if (!verbose) print("${toBeSplit.print()} ")
        val left = toBeSplit!!.value!!.floorDiv(2)
        val right = toBeSplit!!.value!! - left
        toBeSplit.makePair(Pair(Snailfish(left, toBeSplit), Snailfish(right, toBeSplit)))

        return true
    }

    fun getSplitting(): Snailfish? {
        return if (isSplitting()) {
            this
        } else if (isSingle()) {
            null
        } else
            pair?.first?.getSplitting() ?: pair?.second?.getSplitting()
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

    private fun isSingle() = value != null

    private fun isSplitting() = isSingle() && value!! >= 10

    private fun lastPair() = pair != null && pair!!.first.isSingle() && pair!!.second.isSingle()
}