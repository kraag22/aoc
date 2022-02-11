package advent.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TenthTest {
    @Test
    fun `processing line with unexpected closure works`() {
        val t = Tenth()

        assertThat(t.processLine("{([(<{}[<>[]}>{[]{[(<()>")).isEqualTo("}")
        assertThat(t.processLine("[[<[([]))<([[{}[[()]]]")).isEqualTo(")")
        assertThat(t.processLine("[{[{({}]{}}([{[{{{}}([]")).isEqualTo("]")
        assertThat(t.processLine("[<(<(<(<{}))><([]([]()")).isEqualTo(")")
        assertThat(t.processLine("<{([([[(<>()){}]>(<<{{")).isEqualTo(">")
    }

    @Test
    fun `processing line works for incomplete lines`() {
        val t = Tenth()

        assertThat(t.processLine("{(")).isEqualTo("i")
        assertThat(t.processLine("{()")).isEqualTo("i")
        assertThat(t.processLine("{")).isEqualTo("i")
        assertThat(t.processLine("{()()")).isEqualTo("i")
    }

    @Test
    fun `processing line works for complete lines`() {
        val t = Tenth()

        assertThat(t.processLine("{()}")).isEqualTo("o")
        assertThat(t.processLine("{}")).isEqualTo("o")
        assertThat(t.processLine("{[]{}<>}")).isEqualTo("o")
        assertThat(t.processLine("{()()[({})]}")).isEqualTo("o")
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
}