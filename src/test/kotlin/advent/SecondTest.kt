package advent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SecondTest {
    @Test
    fun itWorksTest() {
        val s = Second()
        s.loadFile("/2_example.txt")

        assertThat(s.commands).hasSize(6)
        assertThat(s.commands.first().value).isEqualTo(5)
    }

    @Test
    fun computeSimpleTest() {
        val s = Second()
        s.loadFile("/2_example.txt")

        assertThat(s.computeSimple()).isEqualTo(150)
    }

    @Test
    fun computeSimpleOnRealDataTest() {
        val s = Second()
        s.loadFile("/2.txt")

        assertThat(s.computeSimple()).isEqualTo(2091984)
    }

    @Test
    fun computeWithAimOnExampleDataTest() {
        val s = Second()
        s.loadFile("/2_example.txt")

        assertThat(s.computeWithAim()).isEqualTo(900)
    }

    @Test
    fun computeWithAimOnRealDataTest() {
        val s = Second()
        s.loadFile("/2.txt")

        assertThat(s.computeWithAim()).isEqualTo(2086261056)
    }
}