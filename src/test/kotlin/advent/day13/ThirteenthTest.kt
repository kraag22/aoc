package advent.day13

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ThirteenthTest {
    @Test
    fun `import should work on example data`() {
        val t = Thirteenth()
        t.extract("/13_example.txt")

        assertThat(t.folds).hasSize(2)
        assertThat(t.folds.first()).isEqualTo(Pair("y", 7))
        assertThat(t.points).hasSize(18)
        assertThat(t.points.first()).isEqualTo(Pair(6, 10))
    }

    @Test
    fun `fold point around X axis should work`() {
        val t = Thirteenth()

        assertThat(t.foldPoint("x", 7, Pair(8, 2)))
            .isEqualTo(Pair(6, 2))

        assertThat(t.foldPoint("x", 7, Pair(6, 2)))
            .isEqualTo(Pair(6, 2))
    }

    @Test
    fun `fold point around Y axis should work`() {
        val t = Thirteenth()

        assertThat(t.foldPoint("y", 7, Pair(2, 8)))
            .isEqualTo(Pair(2, 6))

        assertThat(t.foldPoint("y", 7, Pair(2, 6)))
            .isEqualTo(Pair(2, 6))

        assertThat(t.foldPoint("y", 7, Pair(6, 9)))
            .isEqualTo(Pair(6, 5))

        assertThat(t.foldPoint("y", 7, Pair(6, 13)))
            .isEqualTo(Pair(6, 1))

        assertThat(t.foldPoint("y", 7, Pair(2, 14)))
            .isEqualTo(Pair(2, 0))
    }

    @Test
    fun `apply first folds on example data`() {
        val t = Thirteenth()
        t.extract("/13_example.txt")
        t.folds = t.folds.dropLast(1).toMutableList()
        assertThat(t.compute()).isEqualTo(17)
    }

    @Test
    fun `apply all folds on example data`() {
        val t = Thirteenth()
        t.extract("/13_example.txt")

        assertThat(t.compute()).isEqualTo(16)
    }

    @Test
    fun `apply first folds on input data`() {
        val t = Thirteenth()
        t.extract("/13.txt")
        t.folds = mutableListOf(t.folds.first())
        assertThat(t.compute()).isEqualTo(687)
    }

    @Test
    fun `apply all folds on input data`() {
        val t = Thirteenth()
        t.extract("/13.txt")
        assertThat(t.compute()).isEqualTo(98)
    }
}
