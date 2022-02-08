package advent.day4

import advent.Base
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class FourthTest {
    val data = Base.readData("/4.txt")
    val fourth = Fourth()

    init {
        fourth.loadBoards(data)
    }

    @Test
    fun readDataTest() {
        Assertions.assertThat(data.size).isEqualTo(19)
    }

    @Test
    fun readLotteryTickersTest() {
        Assertions.assertThat(fourth.lotteryNumbers).hasSize(27)
    }

    @Test
    fun canLoadBoardsTest() {
        Assertions.assertThat(fourth.boards).hasSize(3)
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
        Assertions.assertThat(matrix.split("\n")).hasSize(5)
        val board = Board(matrix.split("\n"))
        Assertions.assertThat(board.score(1)).isEqualTo(213)
        Assertions.assertThat(board.score(2)).isEqualTo(426)
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
        Assertions.assertThat(matrix.split("\n")).hasSize(5)
        val board = Board(matrix.split("\n"))
        Assertions.assertThat(board.hasBingo()).isTrue
        Assertions.assertThat(board.hasBingoInLine(2)).isTrue
        Assertions.assertThat(board.hasBingoInLine(1)).isFalse
        Assertions.assertThat(board.hasBingoInColumn(4)).isTrue
        Assertions.assertThat(board.hasBingoInColumn(5)).isFalse
    }
}