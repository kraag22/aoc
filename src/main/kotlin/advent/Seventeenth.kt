package advent

import kotlin.math.max


class Seventeenth(val targetX:Pair<Int, Int>, val targetY:Pair<Int, Int>) {

    fun getMaxVelocityForX(): Int {
        var sum = 0
        for (i in (1..targetX.second)) {
            sum += i
            if (sum > targetX.second) {
                return i - 1
            }
        }

        return -1
    }

    fun getMaxVelocityForY(maxX: Int, y: Int): Int {
        val subtract = mapOf(
            1 to 0,
            2 to -1,
            3 to -3,
            4 to -6,
            5 to -10,
            6 to -15,
            7 to -21,
            8 to -28,
            9 to -36,
            10 to -45,
            11 to -55,
            12 to -66,
            13 to -78,
            14 to -91,
            15 to -105,
            16 to -120
        )
        val result = (y - subtract[maxX]!!) / maxX.toFloat()
        return kotlin.math.ceil( result ).toInt()
    }

    fun proccessHits(): Int {
        val xMin = 0
        var yMax = 1250
        var hits = 0

        for (x in xMin..targetX.second) {
            for (y in yMax downTo targetY.first) {
//                println("shooting [$x,$y]")
                if (calculateMaxHeight(x,y) != -1) {
                    println("[$x, $y] hit")
                    hits++
                }
            }
        }
        return hits
    }

    fun process(): Int {
        val xMin = getMaxVelocityForX()
        var y1 = 0
        var y2 = 0
        var yMax = 1250
        var maxHeight = 0

        for (x in xMin..targetX.second) {
//            y1 = getMaxVelocityForY(x, targetY.first)
//            y2 = getMaxVelocityForY(x, targetY.second)
//            yMax = max(y1, y2, 50)
            for (y in yMax downTo 0) {
//                println("shooting [$x,$y]")
                val height = calculateMaxHeight(x,y)
                if (height > maxHeight) {
                    println("[$x, $y] localMax: $height")
                    maxHeight = height
                }
            }
        }
        return maxHeight
    }

    fun calculateMaxHeight(x: Int, y: Int): Int {
        var maxY = 0
        var positionX = 0
        var positionY = 0
        var currentX = x
        var currentY = y

        while (true) {
            positionX += currentX
            positionY += currentY
            maxY = max(maxY, positionY)
            if (hit(positionX, positionY)) {
               return maxY
            }

            if (currentX > 0 ) {
                currentX--
            }
            currentY--

            if (outOfBounds(positionX, positionY)) {
               return -1
            }
        }
    }

    fun outOfBounds(x: Int, y:Int):Boolean {
        return x > targetX.second || y < targetY.first
    }

    fun hit(x: Int, y:Int):Boolean {
        return (targetX.first <= x && x <= targetX.second && targetY.first <= y && y <= targetY.second )
    }
}