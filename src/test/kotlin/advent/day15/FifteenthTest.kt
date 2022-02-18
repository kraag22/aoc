package advent.day15

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FifteenthTest {
    @Test
    fun `extract and print should work`() {
        val f = Fifteenth()
        f.extract("/15_example.txt")
        f.print(f.gridValues)

        assertThat(f.gridValues.size).isEqualTo(10)
        assertThat(f.gridValues.first()).hasSize(10)
    }

    @Test
    fun `neighbours functions should work`() {
        val f = Fifteenth()
        f.extract("/15_example.txt")
        f.gridPath[0][0] = 0
        f.gridPath[1][1] = 1

        assertThat(f.gridPath.getNotVisitedNeighbours(Pair(1, 0))).isEqualTo(
            listOf(Pair(2, 0))
        )

        assertThat(f.gridPath.getNotVisitedNeighbours(Pair(9, 9))).isEqualTo(
            listOf(Pair(8, 9), Pair(9, 8))
        )

        assertThat(f.gridPath.getVisitedNeighbours(Pair(1, 0))).isEqualTo(
            listOf(Pair(0, 0), Pair(1, 1))
        )

        f.gridPath[1][0] = 2
        f.gridPath[2][0] = 3
        f.gridPath.fillPathForPoint(f.gridValues, Pair(2, 1))

        assertThat(f.gridPath[2][1]).isEqualTo(8 + 1)
    }

    @Test
    fun `compute depth first should walk through whole example plane`() {
        val f = Fifteenth()
        f.extract("/15_example.txt")

        f.compute()

        f.print(f.gridPath)
        assertThat(f.getMinPathValue()).isEqualTo(40)
    }

    @Test
    fun `compute depth first should walk through whole input plane`() {
        val f = Fifteenth(100)
        f.extract("/15.txt")

        f.compute()

        f.print(f.gridPath)
        assertThat(f.getMinPathValue()).isEqualTo(462)
    }

}