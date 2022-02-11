package advent.day9

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NinthTest {
    @Test
    fun `should recognize low points`() {
        val n = Ninth()
        n.extract("/9_example.txt")
        val first = n.firstPoint!!

        assertThat(first.isLowest()).isFalse
        assertThat(first.right?.isLowest()).isTrue
        assertThat(first.right?.bottom?.isLowest()).isFalse
    }

    @Test
    fun `find lowest points in example data`() {
        val n = Ninth()
        n.extract("/9_example.txt")
        val first = n.firstPoint!!

        assertThat(n.findLowestPoints(first)).hasSize(4)
        assertThat(n.findLowestPoints(first).map { it.height }).isEqualTo(listOf(1, 0, 5, 5))
    }

    @Test
    fun `getCoordinates should work`() {
        val n = Ninth()
        n.extract("/9_example.txt")
        val first = n.firstPoint!!

        val lowest = n.findLowestPoints(first)
        assertThat(lowest.first().getCoordinates()).isEqualTo(Pair(2, 1))
        assertThat(lowest[1].getCoordinates()).isEqualTo(Pair(10, 1))
        assertThat(lowest[2].getCoordinates()).isEqualTo(Pair(3, 3))
        assertThat(lowest.last().getCoordinates()).isEqualTo(Pair(7, 5))

    }

    @Test
    fun `compute risk level on example data`() {
        val n = Ninth()
        n.extract("/9_example.txt")
        val first = n.firstPoint!!

        assertThat(n.computeRiskLevel(first)).isEqualTo(15)
    }

    @Test
    fun `compute risk level on input data`() {
        val n = Ninth()
        n.extract("/9.txt")
        val first = n.firstPoint!!

        assertThat(n.computeRiskLevel(first)).isEqualTo(458)
    }

    @Test
    fun `compute basins on example data`() {
        val n = Ninth()
        n.extract("/9_example.txt")
        val first = n.firstPoint!!

        assertThat(n.computeBasins(first)).isEqualTo(1134)
    }

    @Test
    fun `compute basins on input data`() {
        val n = Ninth()
        n.extract("/9.txt")
        val first = n.firstPoint!!

        assertThat(n.computeBasins(first)).isEqualTo(1391940)
    }

    @Test
    fun `hunt bug in basin computation`() {
        val n = Ninth()
        n.extract("/9.txt")
        val first = n.firstPoint!!

        val lowest = n.findLowestPoints(first)
            .map { Pair(it, it.getBasinSize()) }
            .sortedBy { it.second }
            .takeLast(3)
            .also { println(it) }

//        lowest[1].first.getBasinSize(n::printer)
    }
}
