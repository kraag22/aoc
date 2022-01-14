package advent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SeventhTest {

    @Test
    fun `Try to load data from file`() {
        val s = Seventh()
        s.loadFile("/7_example.txt")

        assertThat(s.rows).hasSize(10)
    }

    @Test
    fun `Try to load data from param`() {
        val s = Seventh()
        s.loadData("1,2,3,4")

        assertThat(s.rows).isEqualTo(listOf(1,2,3,4))
    }

    @Test
    fun `We can get fuel cost for single column`() {
        val s = Seventh()
        s.loadData("1,2,3,4,5,6")

        assertThat(s.computeFuelForColumnOnePerStep(1)).isEqualTo(15)
    }

    @Test
    fun `Test bruteForce algorithm works for example data`() {
        val s = Seventh()
        s.loadFile("/7_example.txt")

        assertThat(s.bruteForce()).isEqualTo(37)
    }

    @Test
    fun `Test bruteForce algorithm works for input data`() {
        val s = Seventh()
        s.loadFile("/7.txt")

        assertThat(s.bruteForce()).isEqualTo(328187)
    }

    @Test
    fun `Compute fuel cost with increasing spending per move`() {
        val s = Seventh()
        s.loadData("1,4")
        assertThat(s.computeFuelForColumnWithIncreasingCost(1)).isEqualTo(6)

        s.loadData("16,5")
        assertThat(s.computeFuelForColumnWithIncreasingCost(16)).isEqualTo(66)
    }

    @Test
    fun `Test bruteForce algorithm works with increasing fuel per step on example data`() {
        val s = Seventh()
        s.loadFile("/7_example.txt")

        assertThat(s.bruteForce(true)).isEqualTo(168)
    }

    @Test
    fun `Test bruteForce algorithm works with increasing fuel per step on input data`() {
        val s = Seventh()
        s.loadFile("/7.txt")

        assertThat(s.bruteForce(true)).isEqualTo(91257582)
    }
}