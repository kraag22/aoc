package advent.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TenthTest {
    @Test
    fun `processing line with unexpected closure works`() {
        val t = Tenth()

        assertThat(t.processLine("{([(<{}[<>[]}>{[]{[(<()>")).isEqualTo(listOf("}"))
        assertThat(t.processLine("[[<[([]))<([[{}[[()]]]")).isEqualTo(listOf(")"))
        assertThat(t.processLine("[{[{({}]{}}([{[{{{}}([]")).isEqualTo(listOf("]"))
        assertThat(t.processLine("[<(<(<(<{}))><([]([]()")).isEqualTo(listOf(")"))
        assertThat(t.processLine("<{([([[(<>()){}]>(<<{{")).isEqualTo(listOf(">"))
    }

    @Test
    fun `processing line works for incomplete lines`() {
        val t = Tenth()

        assertThat(t.processLine("{(")).isEqualTo(listOf(")", "}"))
        assertThat(t.processLine("{()")).isEqualTo(listOf("}"))
        assertThat(t.processLine("{")).isEqualTo(listOf("}"))
        assertThat(t.processLine("{()()")).isEqualTo(listOf("}"))
    }

    @Test
    fun `processing line works for complete lines`() {
        val t = Tenth()

        assertThat(t.processLine("{()}")).isEmpty()
        assertThat(t.processLine("{}")).isEmpty()
        assertThat(t.processLine("{[]{}<>}")).isEmpty()
        assertThat(t.processLine("{()()[({})]}")).isEmpty()
    }

    @Test
    fun `compute score on example data`() {
        val t = Tenth()
        t.extract("/10_example.txt")

        assertThat(t.computeScore()).isEqualTo(26397)
    }

    @Test
    fun `compute score on input data`() {
        val t = Tenth()
        t.extract("/10.txt")

        assertThat(t.computeScore()).isEqualTo(367059)
    }

    @Test
    fun `compute score on incomplete line`() {
        val t = Tenth()
        assertThat(listOf(")", "}").getScore()).isEqualTo(8)
        assertThat(listOf("]", ")", "}", ">").getScore()).isEqualTo(294)
    }

    @Test
    fun `compute score on incomplete lines in example data`() {
        val t = Tenth()
        t.extract("/10_example.txt")

        assertThat(t.getScoreOnIncompleteLines()).isEqualTo(288957)
    }

    @Test
    fun `compute score on incomplete lines in input data`() {
        val t = Tenth()
        t.extract("/10.txt")

        assertThat(t.getScoreOnIncompleteLines()).isEqualTo(1952146692)
    }
}