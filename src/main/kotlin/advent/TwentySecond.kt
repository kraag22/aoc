package advent

class Cuboid(var x: Pair<Int, Int>, var y: Pair<Int, Int>, var z: Pair<Int, Int>) {
    var isOn = true

    companion object {
        fun parseFromLine(line: String): Cuboid {
            val regex =
                "^(on|off) x=(-?[0-9]+)\\.\\.(-?[0-9]+),y=(-?[0-9]+)\\.\\.(-?[0-9]+),z=(-?[0-9]+)\\.\\.(-?[0-9]+)".toRegex()
            val results = regex.find(line)

            assert(results!!.groups.size == 8)

            val coords = results.groupValues
                .drop(2)
                .map { it.toInt() }
                .toTypedArray()
            val cuboid = Cuboid(Pair(coords[0], coords[1]), Pair(coords[2], coords[3]), Pair(coords[4], coords[5]))

            if (results.groupValues.drop(1).first() == "on") {
                cuboid.turnOn()
            } else {
                cuboid.turnOff()
            }
            return cuboid
        }
    }

    private fun turnOff() {
        isOn = false
    }

    private fun turnOn() {
        isOn = true
    }

    fun isInGrid(maxSize: Int): Boolean {
        if (-maxSize <= x.first && x.first <= maxSize || -maxSize <= x.second && x.second <= maxSize) return true
        if (-maxSize <= y.first && y.first <= maxSize || -maxSize <= y.second && y.second <= maxSize) return true
        if (-maxSize <= z.first && z.first <= maxSize || -maxSize <= z.second && z.second <= maxSize) return true
        return false
    }

    fun shrinkToGrid(maxSize: Int) {
        if (!isInGrid(maxSize)) throw Exception("Cannot shrink cuboid outside the grid")
        val newXfirst = if (x.first < -maxSize) -maxSize else x.first
        val newXsecond = if (x.second > maxSize) maxSize else x.second
        x = Pair(newXfirst, newXsecond)

        val newYfirst = if (y.first < -maxSize) -maxSize else y.first
        val newYsecond = if (y.second > maxSize) maxSize else y.second
        y = Pair(newYfirst, newYsecond)

        val newZfirst = if (z.first < -maxSize) -maxSize else z.first
        val newZsecond = if (z.second > maxSize) maxSize else z.second
        z = Pair(newZfirst, newZsecond)
    }

    override fun toString(): String {
        return listOf(x.toString(), y.toString(), z.toString()).joinToString(",")
    }
}

class TwentySecond(val maxSize: Int) : Base() {
    private val state = Array(2 * maxSize + 1) {
        Array(2 * maxSize + 1) {
            Array(2 * maxSize + 1) { false }
        }
    }

    fun updateStateWith(cuboid: Cuboid) {
        if (!cuboid.isInGrid(maxSize)) {
            println("skipping: $cuboid")
            return
        }

        cuboid.shrinkToGrid(maxSize)

        for (x in (cuboid.x.first..cuboid.x.second)) {
            for (y in (cuboid.y.first..cuboid.y.second)) {
                for (z in (cuboid.z.first..cuboid.z.second)) {
                    updateState(x, y, z, cuboid.isOn)
                }
            }
        }
    }

    fun updateState(x: Int, y: Int, z: Int, onState: Boolean) {
        state[x + maxSize][y + maxSize][z + maxSize] = onState
    }

    fun readCoordinates(name: String): List<Cuboid> {
        val lines = readData("/$name.txt")

        return lines.map { Cuboid.parseFromLine(it) }
    }

    fun calculate(): Int {
        var sum = 0
        for (x in (0..2 * maxSize)) {
            for (y in (0..2 * maxSize)) {
                for (z in (0..2 * maxSize)) {
                    if (state[x][y][z]) {
                        sum++
                    }
                }
            }
        }
        return sum
    }
}