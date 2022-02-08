package advent.day22

import advent.Cuboid
import advent.TwentySecond
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class TwentySecondTest {
    val testLines = TwentySecond(50).readCoordinates("22")

    @Test
    fun readCoordinatesTest() {
        Assertions.assertThat(testLines).hasSize(22)
    }

    @Test
    fun parseFromLineTest() {
        val cuboid = Cuboid.parseFromLine("on x=-27..23,y=-28..26,z=-21..29")

        Assertions.assertThat(cuboid).isInstanceOf(Cuboid::class.java)
        Assertions.assertThat(cuboid.x).isEqualTo(Pair(-27, 23))
        Assertions.assertThat(cuboid.y).isEqualTo(Pair(-28, 26))
        Assertions.assertThat(cuboid.z).isEqualTo(Pair(-21, 29))
    }

    @Test
    fun isInGrid() {
        Assertions.assertThat(Cuboid(Pair(1, 2), Pair(3, 4), Pair(5, 6)).isInGrid(3)).isTrue
        Assertions.assertThat(Cuboid(Pair(11, 12), Pair(13, 14), Pair(15, 16)).isInGrid(10)).isFalse
        Assertions.assertThat(Cuboid(Pair(11, 12), Pair(13, 14), Pair(15, 16)).isInGrid(11)).isTrue
    }

    @Test
    fun shrinkToGrid() {
        val cuboid = Cuboid(Pair(40, 60), Pair(-60, -40), Pair(-500, 500))
        cuboid.shrinkToGrid(50)
        Assertions.assertThat(cuboid.toString()).isEqualTo("(40, 50),(-50, -40),(-50, 50)")
    }

    @Test
    fun checkBorderCuboidTest() {
        val ts = TwentySecond(50)
        val cuboid = Cuboid.parseFromLine("on x=-1..2,y=-1..50,z=-50..-1")
        ts.updateStateWith(cuboid)
        Assertions.assertThat(cuboid.isInGrid(50)).isTrue
        Assertions.assertThat(ts.calculate()).isEqualTo(4 * 52 * 50)
    }

    @Test
    fun exampleTest() {
        val ts = TwentySecond(50)
        val testLines = ts.readCoordinates("22_example")
        Assertions.assertThat(testLines).hasSize(4)

        ts.updateState(3, 3, 3, true)
        Assertions.assertThat(ts.calculate()).isEqualTo(1)

        ts.updateStateWith(Cuboid(Pair(2, 3), Pair(2, 3), Pair(2, 3)))
        Assertions.assertThat(ts.calculate()).isEqualTo(8)
    }

    @Test
    fun runExampleTest() {
        val ts = TwentySecond(50)
        val cuboids = ts.readCoordinates("22_example")
        cuboids.forEach {
            ts.updateStateWith(it)
        }

        Assertions.assertThat(ts.calculate()).isEqualTo(39)
    }

    @Test
    fun runLargerTest() {
        val ts = TwentySecond(50)
        val cuboids = ts.readCoordinates("22")
        cuboids.forEach {
            ts.updateStateWith(it)
        }

        Assertions.assertThat(ts.calculate()).isEqualTo(590784)
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

        Assertions.assertThat(ts.calculate()).isEqualTo(570915)
    }
}