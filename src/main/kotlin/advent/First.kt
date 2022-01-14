package advent

class First: Base() {
    private val depths = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
    fun run() {
        getIncreases(getTriplets(depths))

        val data = readData("/data.txt").map { it.toInt() }
        getIncreases(getTriplets(data))
    }

    private fun getTriplets(data: List<Int>) = data.windowed(3, 1).map {
        println("$it -> ${it.sum()}")
        it.sum()
    }

    private fun getIncreases(data: List<Int>): Int {
        var last = 200000
        var increases = 0
        data.forEach {
            if (it > last) {
                increases++
                println("$last < $it ++")
            } else {
                println("$last > $it")
            }
            last = it
        }

        println("increases: $increases")
        return increases
    }
}