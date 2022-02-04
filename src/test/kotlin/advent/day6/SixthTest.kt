package advent.day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger

internal class SixthTest {

    @Test
    fun `init works`() {
        val s = Sixth()
        s.extract("/6_example.txt")

        assertThat(s.fishes).hasSize(9)
        assertThat(s.fishes[0]).isEqualTo(ShellFish(0, BigInteger.ZERO))
        assertThat(s.fishes[1]).isEqualTo(ShellFish(1, 1.toBigInteger()))
        assertThat(s.fishes[3]).isEqualTo(ShellFish(3, 2.toBigInteger()))
    }

    @Test
    fun `processDay should work`() {
        val s = Sixth()
        s.extract("/6_example.txt")
        s.processDay()

        assertThat(s.fishes[0]).isEqualTo(ShellFish(0, 1.toBigInteger()))
        assertThat(s.fishes[1]).isEqualTo(ShellFish(1, 1.toBigInteger()))
        assertThat(s.fishes[2]).isEqualTo(ShellFish(2, 2.toBigInteger()))
        assertThat(s.fishes[3]).isEqualTo(ShellFish(3, 1.toBigInteger()))
        assertThat(s.fishes[4]).isEqualTo(ShellFish(4, 0.toBigInteger()))
        assertThat(s.fishes[5]).isEqualTo(ShellFish(5, 0.toBigInteger()))
        assertThat(s.fishes[6]).isEqualTo(ShellFish(6, 0.toBigInteger()))
        assertThat(s.fishes[7]).isEqualTo(ShellFish(7, 0.toBigInteger()))
        assertThat(s.fishes[8]).isEqualTo(ShellFish(8, 0.toBigInteger()))

        assertThat(s.countFish()).isEqualTo(5)
    }

    @Test
    fun `process X days should work`() {
        val s = Sixth()
        s.extract("/6_example.txt")
        s.process(4)

        assertThat(s.fishes[0]).isEqualTo(ShellFish(0, 1.toBigInteger()))
        assertThat(s.fishes[1]).isEqualTo(ShellFish(1, 0.toBigInteger()))
        assertThat(s.fishes[2]).isEqualTo(ShellFish(2, 0.toBigInteger()))
        assertThat(s.fishes[3]).isEqualTo(ShellFish(3, 0.toBigInteger()))
        assertThat(s.fishes[4]).isEqualTo(ShellFish(4, 1.toBigInteger()))
        assertThat(s.fishes[5]).isEqualTo(ShellFish(5, 1.toBigInteger()))
        assertThat(s.fishes[6]).isEqualTo(ShellFish(6, 3.toBigInteger()))
        assertThat(s.fishes[7]).isEqualTo(ShellFish(7, 1.toBigInteger()))
        assertThat(s.fishes[8]).isEqualTo(ShellFish(8, 2.toBigInteger()))

        assertThat(s.countFish()).isEqualTo(9)

    }

    @Test
    fun `process test example`() {
        val s = Sixth()
        s.extract("/6_example.txt")
        s.process(18)
        assertThat(s.countFish()).isEqualTo(26)

        val s2 = Sixth()
        s2.extract("/6_example.txt")
        s2.process(80)
        assertThat(s2.countFish()).isEqualTo(5934)
    }

    @Test
    fun `process input data`() {
        val s = Sixth()
        s.extract("/6.txt")
        s.process(80)
        assertThat(s.countFish()).isEqualTo(354564)
    }

    @Test
    fun `process test example 256x`() {
        val s = Sixth()
        s.extract("/6_example.txt")
        s.process(256)
        assertThat(s.countFish()).isEqualTo(26984457539)
    }

    @Test
    fun `process input example 256x`() {
        val s = Sixth()
        s.extract("/6.txt")
        s.process(256)
        assertThat(s.countFish()).isEqualTo(1609058859115)
    }
}
