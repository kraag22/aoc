package advent.day4

import advent.Base

class Fourth : Base() {
    val boards = mutableListOf<Board>()
    var lotteryNumbers = mutableListOf<Int>()

    fun run() {
        println("fourth")
        val data = readData("/4_final.txt")
        loadBoards(data)

        val winningBoards = mutableListOf<Triple<Board, Int, Int>>()

        lotteryNumbers.forEach { lotteryNumber ->
            println("Lottery number: $lotteryNumber")

            boards.forEachIndexed { index, it ->
                if (it.hasBingo()) {
                    return@forEachIndexed
                }
                it.crossNumber(lotteryNumber)
                if (it.hasBingo()) {
                    println("found bingo for: $index")
                    winningBoards.add(Triple(it, index, lotteryNumber))
                    return@forEachIndexed
                }
            }
        }
        val (firstWinning, firstIndex, firstWinningLotteryNumber) = winningBoards.first()
        firstWinning.printBoard()
        println("score:${firstWinning.score(firstWinningLotteryNumber)} number:${firstIndex}")

        val (lastWinning, lastIndex, lastWinningLotteryNumber) = winningBoards.last()
        lastWinning.printBoard()
        println("score:${lastWinning.score(lastWinningLotteryNumber)} number:${lastIndex}")
    }

    fun loadBoards(data: List<String>) {
        lotteryNumbers = data.first().split(",").map { it.toInt() }.toMutableList()
        data.drop(2).windowed(5, 6).forEach {
            boards.add(Board(it))
        }
    }
}

class Board(numbers: List<String>) {
    private val data = Array(25) { 0 }

    init {
        var index = 0
        check(numbers.size == 5)

        numbers.forEach { line ->
            val five = trimSpaces(line).split(" ")
            check(five.size == 5)

            five.forEach {
                data[index] = it.toInt()
                index++
            }
        }
    }

    fun score(number: Int): Int {
        var sum = 0
        data.forEach {
            if (it > 0) {
                sum += it
            }
        }
        return sum * number
    }

    fun hasBingo(): Boolean {
        var bingo = false
        for (i in 1..5) {
            if (hasBingoInLine(i)) {
                bingo = true
                break
            }
            if (hasBingoInColumn(i)) {
                bingo = true
                break
            }
        }
        return bingo
    }

    fun crossNumber(number: Int) {
        while (true) {
            val index = data.indexOf(number)
            if (index == -1) break

            data[index] = -1
        }
    }

    fun hasBingoInLine(lineNo: Int): Boolean {
        val start = (lineNo - 1) * 5
        val line = data.filterIndexed { index, value -> index >= start && index < start + 5 && value == -1 }
        return line.size == 5
    }

    fun hasBingoInColumn(columnNo: Int): Boolean {
        val start = columnNo - 1
        val line = data.filterIndexed { index, value -> index.mod(5) == start && value == -1 }
        return line.size == 5
    }

    private fun trimSpaces(input: String): String {
        return input.replace("\\s+".toRegex(), " ").trim()
    }

    fun printBoard() {
        println("------------")
        data.toList().windowed(5, 5).forEach { println("${it.joinToString(" ")},") }
    }
}