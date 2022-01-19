package advent

class Third: Base() {
    var columns = mutableListOf<ByteArray>()

    override fun parseAndStore(lines: List<String>) {
        val numberLength = lines[0].length
        val linesLength = lines.size

        for (i in (0 until numberLength)) {
           val byteArray = ByteArray(linesLength)
           lines.forEachIndexed { index, line ->
               byteArray[index] = if (line[i] == '0') 0 else 1
           }
            columns.add(byteArray)
        }
    }

    fun getGamma(): String {
        var gamma = ""
        for(column in columns) {
            val groups = column.groupBy { it }
            gamma += if (groups[0]!!.size > groups[1]!!.size) "0" else "1"
        }

        return gamma
    }

    fun getEpsilon(gamma: String): String {
        return gamma.toList().map { if (it == '0') '1' else '0'}.joinToString("")
    }

    private fun getConsumption(gamma: String, epsilon: String): Int {
        val intGamma = Integer.parseInt(gamma, 2)
        val intEpsilon = Integer.parseInt(epsilon, 2)
        return intGamma * intEpsilon
    }

    fun compute(): Int {
        val gamma = getGamma()
        return getConsumption(gamma, getEpsilon(gamma))
    }
}

fun ByteArray.toInt() = Integer.parseInt(joinToString(""), 2)

fun ByteArray.print() = joinToString("")
