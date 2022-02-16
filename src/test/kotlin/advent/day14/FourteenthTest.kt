package advent.day14

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger

internal class FourteenthTest {
    @Test
    fun `extract should work`() {
        val f = Fourteenth()
        f.extract("/14_example.txt")

        assertThat(f.polymerBase).isEqualTo("NNCB")
        assertThat(f.rules).hasSize(16)
    }

    @Test
    fun `toCouple() should work`() {
        assertThat("NNCB".toCouples()).isEqualTo(
            mapOf(
                "NN" to 1.toBigInteger(),
                "NC" to 1.toBigInteger(),
                "CB" to 1.toBigInteger()
            )
        )

        assertThat("NNCNN".toCouples()).isEqualTo(
            mapOf(
                "NN" to 2.toBigInteger(),
                "NC" to 1.toBigInteger(),
                "CN" to 1.toBigInteger(),
            )
        )
    }

    @Test
    fun `addCouple should work`() {
        val map = mutableMapOf<String, BigInteger>()
        map.addOrSet("NN", 1.toBigInteger())
        assertThat(map).isEqualTo(mutableMapOf("NN" to 1.toBigInteger()))

        map.addOrSet("NN", 2.toBigInteger())
        assertThat(map).isEqualTo(mutableMapOf("NN" to 3.toBigInteger()))

        map.addOrSet("NA", 1.toBigInteger())
        assertThat(map).isEqualTo(
            mutableMapOf(
                "NN" to 3.toBigInteger(),
                "NA" to 1.toBigInteger()
            )
        )
    }

    @Test
    fun `doStep should work`() {
        val f = Fourteenth()
        f.extract("/14_example.txt")

        val res = f.doStep("NNCB".toCouples())
        assertThat(res).isEqualTo("NCNBCHB".toCouples())

        assertThat(f.doStep(res)).isEqualTo("NBCCNBBBCBHCB".toCouples())

        assertThat(f.doStep("NBCCNBBBCBHCB".toCouples()))
            .isEqualTo("NBBBCNCCNBBNBNBBCHBHHBCHB".toCouples())

        assertThat(f.doStep("NBBBCNCCNBBNBNBBCHBHHBCHB".toCouples()))
            .isEqualTo("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB".toCouples())
    }

    @Test
    fun `getScore should work`() {
        val f = Fourteenth()
        f.extract("/14_example.txt")
        assertThat(f.getScore("NNCB".toCouples()))
            .isEqualTo(BigInteger.ONE)

        assertThat(f.getScore("NCNBCHB".toCouples()))
            .isEqualTo(BigInteger.ONE)

        assertThat(f.getScore("NBCCNBBBCBHCB".toCouples()))
            .isEqualTo(5.toBigInteger())
    }

    @Test
    fun `run multiple steps and get score`() {
        val f = Fourteenth()
        f.extract("/14_example.txt")

        assertThat(f.getScoreAfter(1)).isEqualTo(1.toBigInteger())
        assertThat(f.getScoreAfter(2)).isEqualTo(5.toBigInteger())
        assertThat(f.getScoreAfter(10)).isEqualTo(1588.toBigInteger())
    }

    @Test
    fun `run multiple steps and get score on input data`() {
        val f = Fourteenth()
        f.extract("/14.txt")

        assertThat(f.getScoreAfter(10)).isEqualTo(2375.toBigInteger())
    }

    @Test
    fun `run 40 steps and get score on example data`() {
        val f = Fourteenth()
        f.extract("/14_example.txt")

        assertThat(f.getScoreAfter(40)).isEqualTo(2188189693529.toBigInteger())
    }

    @Test
    fun `run 40 steps and get score on input data`() {
        val f = Fourteenth()
        f.extract("/14.txt")

        assertThat(f.getScoreAfter(40)).isEqualTo(1976896901756.toBigInteger())
    }
}