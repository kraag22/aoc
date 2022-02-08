package advent.day3

import advent.Base

class Third : Base() {
    var columns = mutableListOf<ByteArray>()
    private var numbers = mutableListOf<String>()
    private var numberLength: Int = 0
    private var linesLength: Int = 0

    override fun parseAndStore(lines: List<String>) {
        numberLength = lines[0].length
        linesLength = lines.size

        for (i in (0 until numberLength)) {
            val byteArray = ByteArray(linesLength)
            lines.forEachIndexed { index, line ->
                byteArray[index] = if (line[i] == '0') 0 else 1
            }
            columns.add(byteArray)
        }

        numbers = lines.toMutableList()
    }

    fun getGamma(): String {
        var gamma = ""
        for (column in columns) {
            val groups = column.groupBy { it }
            gamma += if (groups[0]!!.size > groups[1]!!.size) "0" else "1"
        }

        return gamma
    }

    fun getEpsilon(gamma: String): String {
        return gamma.toList().map { if (it == '0') '1' else '0' }.joinToString("")
    }

    private fun getConsumption(gamma: String, epsilon: String) = gamma.binaryToInt() * epsilon.binaryToInt()

    fun compute(): Int {
        val gamma = getGamma()
        return getConsumption(gamma, getEpsilon(gamma))
    }

    fun getOxygen(biggerIsStronger: Boolean = true): String {
        var members = mutableSetOf<Int>()
        members.addAll((0 until numbers.size))

        for (column in columns) {
            val zeroes = mutableSetOf<Int>()
            val ones = mutableSetOf<Int>()

            members.forEach { index ->
                if (column[index].toInt() == 0) zeroes.add(index) else ones.add(index)
            }
            members = if (biggerIsStronger) {
                if (zeroes.size > ones.size) zeroes else ones
            } else {
                if (zeroes.size < ones.size) zeroes else ones
            }
            if (zeroes.size == ones.size) {
                members = if (biggerIsStronger) ones else zeroes
            }

            if (members.size == 1) break
        }

        if (members.size != 1) throw Exception("Didn't finish with one number")
        return numbers[members.first()]
    }

    fun getCO2Rating(): String {
        return getOxygen(false)
    }

    fun funGetLifeSupportRating(): Int {
        val intOxygen = getOxygen().binaryToInt()
        val intCO2 = getCO2Rating().binaryToInt()
        return intOxygen * intCO2
    }
}

fun ByteArray.toInt() = Integer.parseInt(joinToString(""), 2)

fun ByteArray.print() = joinToString("")

fun String.binaryToInt() = Integer.parseInt(this, 2)
