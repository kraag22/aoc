package advent.day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ThirdTest {
    @Test
    fun tryBinaryParsing() {
        assertThat(3.toUInt().toString(radix = 2)).isEqualTo("11")
        val byteArray = ByteArray(6) {
            if (it.mod(2) == 0) {
                0
            } else {
                1
            }
        }
        assertThat(byteArray.groupBy { it }[0]).hasSize(3)
        assertThat(byteArray.groupBy { it }[1]).hasSize(3)
        assertThat(byteArray.print()).isEqualTo("010101")
        assertThat(byteArray.toInt()).isEqualTo(21)

    }

    @Test
    fun `Test storage of data`() {
        val t = Third()
        t.extract("/3_example.txt")

        assertThat(t.columns).hasSize(5)
        assertThat(t.columns[0].print()).isEqualTo("011110011100")
    }

    @Test
    fun gammaTest() {
        val t = Third()
        t.extract("/3_example.txt")

        assertThat(t.getGamma()).isEqualTo("10110")
    }

    @Test
    fun epsilonTest() {
        val t = Third()
        assertThat(t.getEpsilon("10110")).isEqualTo("01001")
    }

    @Test
    fun computeExampleTest() {
        val t = Third()
        t.extract("/3_example.txt")
        assertThat(t.compute()).isEqualTo(198)
    }

    @Test
    fun computeFirstPartTest() {
        val t = Third()
        t.extract("/3.txt")
        assertThat(t.compute()).isEqualTo(845186)
    }

    @Test
    fun `oxygen generator rating on example data`() {
        val t = Third()
        t.extract("/3_example.txt")

        assertThat(t.getOxygen()).isEqualTo("10111")
    }

    @Test
    fun `CO2 scrubber rating on example data`() {
        val t = Third()
        t.extract("/3_example.txt")

        assertThat(t.getCO2Rating()).isEqualTo("01010")
    }

    @Test
    fun `get life support rating on example data`() {
        val t = Third()
        t.extract("/3_example.txt")

        assertThat(t.funGetLifeSupportRating()).isEqualTo(230)
    }

    @Test
    fun `get life support on real data`() {
        val t = Third()
        t.extract("/3.txt")

        assertThat(t.funGetLifeSupportRating()).isEqualTo(4636702)
    }
}