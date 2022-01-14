package advent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TwentySecondTest {
    val testLines = TwentySecond(50).readCoordinates("22")

    @Test
    fun readCoordinatesTest() {
        assertThat(testLines).hasSize(22)
    }

    @Test
    fun parseFromLineTest() {
        val cuboid = Cuboid.parseFromLine("on x=-27..23,y=-28..26,z=-21..29")

        assertThat(cuboid).isInstanceOf(Cuboid::class.java)
        assertThat(cuboid.x).isEqualTo(Pair(-27, 23))
        assertThat(cuboid.y).isEqualTo(Pair(-28, 26))
        assertThat(cuboid.z).isEqualTo(Pair(-21, 29))
    }

    @Test
    fun isInGrid() {
        assertThat(Cuboid(Pair(1, 2), Pair(3, 4), Pair(5, 6)).isInGrid(3)).isTrue
        assertThat(Cuboid(Pair(11, 12), Pair(13, 14), Pair(15, 16)).isInGrid(10)).isFalse
        assertThat(Cuboid(Pair(11, 12), Pair(13, 14), Pair(15, 16)).isInGrid(11)).isTrue
    }

    @Test
    fun shrinkToGrid() {
        val cuboid = Cuboid(Pair(40, 60), Pair(-60, -40), Pair(-500, 500))
        cuboid.shrinkToGrid(50)
        assertThat(cuboid.toString()).isEqualTo("(40, 50),(-50, -40),(-50, 50)")
    }

    @Test
    fun checkBorderCuboidTest() {
        val ts = TwentySecond(50)
        val cuboid = Cuboid.parseFromLine("on x=-1..2,y=-1..50,z=-50..-1")
        ts.updateStateWith(cuboid)
        assertThat(cuboid.isInGrid(50)).isTrue
        assertThat(ts.calculate()).isEqualTo(4 * 52 * 50)
    }

    @Test
    fun exampleTest() {
        val ts = TwentySecond(50)
        val testLines = ts.readCoordinates("22_example")
        assertThat(testLines).hasSize(4)

        ts.updateState(3, 3, 3, true)
        assertThat(ts.calculate()).isEqualTo(1)

        ts.updateStateWith(Cuboid(Pair(2, 3), Pair(2, 3), Pair(2, 3)))
        assertThat(ts.calculate()).isEqualTo(8)
    }

    @Test
    fun runExampleTest() {
        val ts = TwentySecond(50)
        val cuboids = ts.readCoordinates("22_example")
        cuboids.forEach {
            ts.updateStateWith(it)
        }

        assertThat(ts.calculate()).isEqualTo(39)
    }

    @Test
    fun runLargerTest() {
        val ts = TwentySecond(50)
        val cuboids = ts.readCoordinates("22")
        cuboids.forEach {
            ts.updateStateWith(it)
        }

        assertThat(ts.calculate()).isEqualTo(590784)
    }

    @Test
    fun runFirstPartTest() {
        val ts = TwentySecond(50)
        val cuboids = ts.readCoordinates("22_first")
        cuboids.forEach {
//            val time = System.currentTimeMillis()
            ts.updateStateWith(it)
//            println("${System.currentTimeMillis() - time}")
        }

        assertThat(ts.calculate()).isEqualTo(570915)
    }
}