package advent.day12

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class TwelfthTest {
    @Test
    fun `load data`() {
        val t = Twelfth()
        t.extract("/12_example.txt")
        assertThat(t.graph).hasSize(6)
        assertThat(t.graph["start"]?.paths).hasSize(2)
    }

    @Test
    fun `get valid jumps should work on example data`() {
        val t = Twelfth()
        t.extract("/12_example.txt")

        assertThat(t.getValidJumps(listOf("start")))
            .isEqualTo(listOf("A", "b"))
        assertThat(t.getValidJumps(listOf("start", "A")))
            .isEqualTo(listOf("c", "b", "end"))
        assertThat(t.getValidJumps(listOf("start", "A", "b")))
            .isEqualTo(listOf("A", "d", "end"))
        assertThat(t.getValidJumps(listOf("start", "b", "A")))
            .isEqualTo(listOf("c", "end"))
        assertThat(t.getValidJumps(listOf("start", "b", "A", "c")))
            .isEqualTo(listOf("A"))
        assertThat(t.getValidJumps(listOf("start", "b", "d")))
            .isEqualTo(emptyList<String>())

        assertThatThrownBy {
            t.getValidJumps(listOf("start", "b", "end"))
        }.hasMessageContaining("Cannot get jumps for end point")

    }

    @Test
    fun `one step od recursion should work`() {
        val t = Twelfth()
        t.extract("/12_example.txt")
        t.doStep(listOf("start", "A", "end"))

        assertThat(t.computedPaths).hasSize(1)
        assertThat(t.computedPaths.first()).isEqualTo(listOf("start", "A", "end"))
    }

    @Test
    fun `supporting functions should work`() {
        assertThat(Pair("a", "b").contains("b")).isTrue
        assertThat(Pair("a", "b").contains("a")).isTrue
        assertThat(Pair("a", "b").contains("c")).isFalse

        assertThat(Pair("a", "b").getOther("a")).isEqualTo("b")
        assertThat(Pair("a", "b").getOther("b")).isEqualTo("a")

        assertThat("ab".isLittle()).isTrue
        assertThat("B".isLittle()).isFalse

        assertThat(Twelfth.Point("ahoj").isLittle()).isTrue
        assertThat(Twelfth.Point("B").isLittle()).isFalse
    }

    @Test
    fun `compute paths on example data`() {
        val t = Twelfth()
        t.extract("/12_example.txt")
        t.compute()

        assertThat(t.computedPaths).hasSize(10)
        assertThat(t.computedPaths).isEqualTo(
            listOf(
                listOf("start", "A", "c", "A", "b", "A", "end"),
                listOf("start", "A", "c", "A", "b", "end"),
                listOf("start", "A", "c", "A", "end"),
                listOf("start", "A", "b", "A", "c", "A", "end"),
                listOf("start", "A", "b", "A", "end"),
                listOf("start", "A", "b", "end"),
                listOf("start", "A", "end"),
                listOf("start", "b", "A", "c", "A", "end"),
                listOf("start", "b", "A", "end"),
                listOf("start", "b", "end")
            )
        )
    }

    @Test
    fun `compute paths on example2 data`() {
        val t = Twelfth()
        t.extract("/12_example2.txt")
        t.compute()

        assertThat(t.computedPaths).hasSize(19)
    }

    @Test
    fun `compute paths on example3 data`() {
        val t = Twelfth()
        t.extract("/12_example3.txt")
        t.compute()

        assertThat(t.computedPaths).hasSize(226)
    }

    @Test
    fun `compute paths on input data`() {
        val t = Twelfth()
        t.extract("/12.txt")
        t.compute()

        assertThat(t.computedPaths).hasSize(5157)
    }

    @Test
    fun `get valid jumps should work on example data - extended`() {
        val t = Twelfth()
        t.extract("/12_example.txt")
        t.enableOneLittleTwice = true

        assertThat(t.getValidJumps(listOf("start")))
            .isEqualTo(listOf("A", "b"))
        assertThat(t.getValidJumps(listOf("start", "A")))
            .isEqualTo(listOf("c", "b", "end"))
        assertThat(t.getValidJumps(listOf("start", "A", "b")))
            .isEqualTo(listOf("A", "d", "end"))
        assertThat(t.getValidJumps(listOf("start", "b", "A")))
            .isEqualTo(listOf("c", "b", "end"))
        assertThat(t.getValidJumps(listOf("start", "b", "A", "c")))
            .isEqualTo(listOf("A"))
        assertThat(t.getValidJumps(listOf("start", "b", "d")))
            .isEqualTo(listOf("b"))
        assertThat(t.getValidJumps(listOf("start", "b", "d", "b")))
            .isEqualTo(listOf("A", "end"))
        assertThat(t.getValidJumps(listOf("start", "b", "A")))
            .isEqualTo(listOf("c", "b", "end"))
        assertThat(t.getValidJumps(listOf("start", "b", "A", "b", "A")))
            .isEqualTo(listOf("c", "end"))

        assertThatThrownBy {
            t.getValidJumps(listOf("start", "b", "end"))
        }.hasMessageContaining("Cannot get jumps for end point")

    }

    @Test
    fun `compute paths on example data - extended`() {
        val t = Twelfth()
        t.extract("/12_example.txt")
        t.enableOneLittleTwice = true
        t.compute()

        assertThat(t.computedPaths).hasSize(36)
    }

    @Test
    fun `compute paths on example2 data - extended`() {
        val t = Twelfth()
        t.extract("/12_example2.txt")
        t.enableOneLittleTwice = true
        t.compute()

        assertThat(t.computedPaths).hasSize(103)
    }

    @Test
    fun `compute paths on example3 data - extended`() {
        val t = Twelfth()
        t.extract("/12_example3.txt")
        t.enableOneLittleTwice = true
        t.compute()

        assertThat(t.computedPaths).hasSize(3509)
    }

    @Test
    fun `compute paths on input data - extended`() {
        val t = Twelfth()
        t.extract("/12.txt")
        t.enableOneLittleTwice = true
        t.compute()

        assertThat(t.computedPaths).hasSize(144309)
    }
}