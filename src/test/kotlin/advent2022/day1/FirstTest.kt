package advent2022.day1

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FirstTest {
    @Test
    fun `load`() {
        val f = First()
        f.extract("/2022/1_example.txt")

        assertThat(f.calories).hasSize(5)
    }

    @Test
    fun `find max group`() {
        val f = First()
        f.extract("/2022/1_example.txt")

        assertThat(f.findMax()).isEqualTo(24_000)
        assertThat(f.findMaxThree()).isEqualTo(45_000)

    }

    @Test
    fun `find max group in first part`() {
        val f = First()
        f.extract("/2022/1.txt")

        assertThat(f.findMax()).isEqualTo(71_506)
        assertThat(f.findMaxThree()).isEqualTo(209_603)
    }
}