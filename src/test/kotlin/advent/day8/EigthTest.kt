package advent.day8

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EigthTest {
    @Test
    fun `get unique values from example data`() {
        val e = Eigth()
        e.extract("/8_example.txt")

        assertThat(e.getUniqueValues()).hasSize(26)
    }

    @Test
    fun `get unique values from input data`() {
        val e = Eigth()
        e.extract("/8.txt")

        assertThat(e.getUniqueValues()).hasSize(284)
    }

    @Test
    fun `translate should work`() {
        val e = Eigth()
        val translationTable =
            e.translateSignals("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab".split(" "))
        assertThat(translationTable).isEqualTo(
            mapOf(
                "acedgfb".toSet() to 8,
                "cdfbe".toSet() to 5,
                "gcdfa".toSet() to 2,
                "fbcad".toSet() to 3,
                "dab".toSet() to 7,
                "cefabd".toSet() to 9,
                "cdfgeb".toSet() to 6,
                "eafb".toSet() to 4,
                "cagedb".toSet() to 0,
                "ab".toSet() to 1,
            )
        )

        assertThat(e.translateValues(translationTable, listOf("cdfeb", "fcadb", "cdfeb", "cdbaf"))).isEqualTo(5353)
    }

    @Test
    fun `translateRow should work`() {
        val e = Eigth()
        e.extract("/8_example.txt")
        assertThat(e.translate(e.data.first())).isEqualTo(8394)
    }

    @Test
    fun `compute example data should work`() {
        val e = Eigth()
        e.extract("/8_example.txt")
        assertThat(e.compute()).isEqualTo(61229)
    }

    @Test
    fun `compute input data`() {
        val e = Eigth()
        e.extract("/8.txt")
        assertThat(e.compute()).isEqualTo(973499)
    }
}