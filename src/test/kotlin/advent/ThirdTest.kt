package advent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ThirdTest {
    @Test
    fun tryBinaryParsing() {
        assertThat(3.toUInt().toString(radix = 2)).isEqualTo("11")
        val byteArray = ByteArray(6) {
            if (it.mod(2) == 0) {
                0
            }
            else {
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
}


