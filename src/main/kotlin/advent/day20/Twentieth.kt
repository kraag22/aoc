package advent.day20

import advent.Base

class Twentieth : Base() {
    var enhancement = listOf<Char>()
    var inputImage = mutableListOf<List<Char>>()
    var defaultPoint = '.'
    override fun parseAndStore(lines: List<String>) {
        enhancement = lines.first().toList()

        lines.drop(2).forEach {
            inputImage.add(it.toList())
        }
    }

    fun enhanceImage(): List<List<Char>> {
        val outputImage = mutableListOf<List<Char>>()
        for (y in (-1..inputImage.yMax)) {
            val line = mutableListOf<Char>()
            for (x in (-1..inputImage.xMax)) {
                line.add(computeOutputPoint(x, y))
            }
            outputImage.add(line)
        }
        return outputImage
    }

    fun twiceEnhance(): List<List<Char>> {
        inputImage = enhanceImage().toMutableList()
        // if zero index of enhancement is #, all the surrounding pixels will be fliped to #. after first run
        // On second run, we need to count with that
        if (enhancement.first() == '#') defaultPoint = '#'
        return enhanceImage()
    }

    fun computeOutputPoint(x: Int, y: Int): Char {
        val idx = getEnhancementIndex(x, y)
        return enhancement[idx]
    }

    fun getInputPoint(x: Int, y: Int): Char {
        val outOfBoundsX = x < 0 || x >= inputImage.xMax
        val outOfBoundsY = y < 0 || y >= inputImage.yMax
        if (outOfBoundsX || outOfBoundsY) {
            return defaultPoint
        }

        return inputImage[y][x]
    }

    fun getEnhancementIndex(x: Int, y: Int): Int {
        val coords = listOf(
            Pair(x - 1, y - 1),
            Pair(x, y - 1),
            Pair(x + 1, y - 1),
            Pair(x - 1, y),
            Pair(x, y),
            Pair(x + 1, y),
            Pair(x - 1, y + 1),
            Pair(x, y + 1),
            Pair(x + 1, y + 1),
        )

        return coords
            .map { getInputPoint(it.x, it.y) }
            .fold("") { acc, letter -> acc + parseLetter(letter) }
            .toInt(2)
    }

    private fun parseLetter(letter: Char) = when (letter) {
        '#' -> "1"
        '.' -> "0"
        else -> error("Cannot contain letter: $letter")
    }
}

val Pair<Int, Int>.x get() = first
val Pair<Int, Int>.y get() = second

val List<List<Char>>.xMax get() = first().size
val List<List<Char>>.yMax get() = size

fun List<List<Char>>.print() {
    for (y in (0 until yMax)) {
        val line = get(y)
        for (x in (0 until xMax)) {
            print(line[x])
        }
        println()
    }
    println()
}

fun List<List<Char>>.countHashes(): Int {
    var sum = 0
    for (y in (0 until yMax)) {
        sum += get(y).filter { it == '#' }.size
    }
    return sum
}