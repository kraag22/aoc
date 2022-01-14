package advent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FourthTest {
    val data = Base.readData("/4.txt")
    val fourth = Fourth()
    init {
        fourth.loadBoards(data)
    }

    @Test
    fun readDataTest() {
        assertThat(data.size).isEqualTo(19)
    }

    @Test
    fun readLotteryTickersTest() {
        assertThat(fourth.lotteryNumbers).hasSize(27)
    }

    @Test
    fun canLoadBoardsTest() {
        assertThat(fourth.boards).hasSize(3)
    }

    @Test
    fun boardScoreTest() {
        val matrix = """
             3 15  0  14 22
             -1 -1 -1 -1  -1
            19  8  7 -1 23
            20 11 10 -1  4
            14 21 16 -1  6
        """.trimIndent()
        assertThat(matrix.split("\n")).hasSize(5)
        val board = Board(matrix.split("\n"))
        assertThat(board.score(1)).isEqualTo(213)
        assertThat(board.score(2)).isEqualTo(426)
    }

    @Test
    fun hasBingoInTest() {
        val matrix = """
             3 15  0  -1 22
             -1 -1 -1 -1  -1
            19  8  7 -1 23
            20 11 10 -1  4
            14 21 16 -1  6
        """.trimIndent()
        assertThat(matrix.split("\n")).hasSize(5)
        val board = Board(matrix.split("\n"))
        assertThat(board.hasBingo()).isTrue
        assertThat(board.hasBingoInLine(2)).isTrue
        assertThat(board.hasBingoInLine(1)).isFalse
        assertThat(board.hasBingoInColumn(4)).isTrue
        assertThat(board.hasBingoInColumn(5)).isFalse
    }
}