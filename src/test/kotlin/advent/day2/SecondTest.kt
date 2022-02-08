package advent.day2

import advent.Second
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class SecondTest {
    @Test
    fun itWorksTest() {
        val s = Second()
        s.extract("/2_example.txt")

        Assertions.assertThat(s.commands).hasSize(6)
        Assertions.assertThat(s.commands.first().value).isEqualTo(5)
    }

    @Test
    fun computeSimpleTest() {
        val s = Second()
        s.extract("/2_example.txt")

        Assertions.assertThat(s.computeSimple()).isEqualTo(150)
    }

    @Test
    fun computeSimpleOnRealDataTest() {
        val s = Second()
        s.extract("/2.txt")

        Assertions.assertThat(s.computeSimple()).isEqualTo(2091984)
    }

    @Test
    fun computeWithAimOnExampleDataTest() {
        val s = Second()
        s.extract("/2_example.txt")

        Assertions.assertThat(s.computeWithAim()).isEqualTo(900)
    }

    @Test
    fun computeWithAimOnRealDataTest() {
        val s = Second()
        s.extract("/2.txt")

        Assertions.assertThat(s.computeWithAim()).isEqualTo(2086261056)
    }
}