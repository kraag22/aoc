package advent.day11

import advent.Base

class Eleventh(private val gridSize: Int = 10) : Base() {
    val grid = Array(gridSize) {
        Array(gridSize) {
            -1
        }
    }

    fun increase(x: Int, y: Int, canIncreaseZero: Boolean = false) {
        if (x < 0 || x >= gridSize) return
        if (y < 0 || y >= gridSize) return
        if (!canIncreaseZero && grid[x][y] == 0) return

        grid[x][y]++
    }

    fun increaseAll() {
        for (x in grid.indices) {
            for (y in grid.indices) {
                increase(x, y, true)
            }
        }
    }

    fun tryFlashing(x: Int, y: Int): Boolean {
        if (x < 0 || x >= gridSize) return false
        if (y < 0 || y >= gridSize) return false

        if (grid[x][y] >= gridSize) {
            getNeighbours(x, y).forEach {
                increase(it.first, it.second)
            }
            grid[x][y] = 0
            return true
        }
        return false
    }

    fun doOneStep(throwOnAllFlushing: Boolean = false): Int {
        val pointsToCheck = mutableListOf<Pair<Int, Int>>()
        var flashesNo = 0
        increaseAll()

        for (x in grid.indices) {
            for (y in grid.indices) {
                if (tryFlashing(x, y)) {
                    flashesNo++
                    pointsToCheck.add(Pair(x, y))
                }
            }
        }

        while (pointsToCheck.size > 0) {
            val point = pointsToCheck.removeFirst()
            getNeighbours(point.first, point.second).forEach {
                if (tryFlashing(it.first, it.second)) {
                    flashesNo++
                    pointsToCheck.add(Pair(it.first, it.second))
                }
            }
        }

        if (throwOnAllFlushing) {
            var summary = 0
            for (x in grid.indices) {
                for (y in grid.indices) {
                    summary += grid[x][y]
                }
            }

            if (summary == 0) {
                throw Exception("All zeroes")
            }
        }

        return flashesNo
    }

    fun doSteps(steps: Int): Int {
        var sum = 0
        repeat(steps) {
            sum += doOneStep()
        }

        return sum
    }

    fun findAllFlashing(): Int {
        repeat(500) {
            try {
                doOneStep(true)
            } catch (e: Exception) {
                return it + 1
            }
        }

        return 0
    }

    private fun getNeighbours(x: Int, y: Int): List<Pair<Int, Int>> {
        val points = mutableListOf<Pair<Int, Int>>()

        if (x > 0 && y > 0) points.add(Pair(x - 1, y - 1))
        if (x > 0) points.add(Pair(x - 1, y))
        if (x > 0 && y < gridSize - 1) points.add(Pair(x - 1, y + 1))

        if (y > 0) points.add(Pair(x, y - 1))
        if (y < gridSize - 1) points.add(Pair(x, y + 1))

        if (x < gridSize - 1 && y > 0) points.add(Pair(x + 1, y - 1))
        if (x < gridSize - 1) points.add(Pair(x + 1, y))
        if (x < gridSize - 1 && y < gridSize - 1) points.add(Pair(x + 1, y + 1))

        return points
    }

    fun load(filename: String) {
        val lines = readData(filename)
        storeLines(lines)
    }

    fun storeLines(lines: List<String>) {
        var lineNo = 0
        lines.forEach { line ->
            val numbers = line.chunked(1)
            var numberNo = 0
            numbers.forEach {
                grid[lineNo][numberNo] = it.toInt()
                numberNo++
            }
            lineNo++
        }
    }

    fun printGrid() {
        for (i in grid.indices) {
            var line = ""
            for (j in grid[i].indices) {
                line += grid[i][j]
            }
            println(line)
        }
    }
}