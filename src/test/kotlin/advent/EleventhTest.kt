package advent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EleventhTest {
    @Test
    fun eleventhClassTest() {
        val el = Eleventh()
        assertThat(el).isInstanceOf(Eleventh::class.java)
    }

    @Test
    fun loadTest() {
        val el = Eleventh(10)
        assertThat(el.grid[0][0]).isEqualTo(-1)
        assertThat(el.grid[9][9]).isEqualTo(-1)

        el.load("/11_intro.txt")
        assertThat(el.grid[0][0]).isEqualTo(5)
        assertThat(el.grid[9][9]).isEqualTo(6)
    }

    @Test
    fun increaseTest() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")
        el.increase(0, 0)
        el.increase(-1, 0)
        el.increase(0, -1)
        el.increase(0, 10)
        el.increase(10, 0)
        assertThat(el.grid[0][0]).isEqualTo(6)
        el.grid[0][0] = 0
        el.increase(0, 0)
        assertThat(el.grid[0][0]).isEqualTo(0)

        el.increase(0, 0, true)
        assertThat(el.grid[0][0]).isEqualTo(1)

        el.increaseAll()
        assertThat(el.grid[3][3]).isEqualTo(2)
        assertThat(el.grid[9][9]).isEqualTo(7)
    }

    @Test
    fun tryFlashingWorkWhenNothingHappensTest() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")
        assertThat(el.tryFlashing(0, 0)).isFalse
        assertThat(el.grid[0][0]).isEqualTo(5)
        assertThat(el.grid[0][1]).isEqualTo(4)
        assertThat(el.grid[1][0]).isEqualTo(2)

        assertThat(el.tryFlashing(-10, -10)).isFalse
        assertThat(el.tryFlashing(10, 110)).isFalse
    }

    @Test
    fun tryFlashingWorksOnEdgeTest() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")
        el.grid[0][0] = 10

        assertThat(el.tryFlashing(0, 0)).isTrue
        assertThat(el.grid[0][0]).isEqualTo(0)
        assertThat(el.grid[0][1]).isEqualTo(5)
        assertThat(el.grid[1][0]).isEqualTo(3)
        assertThat(el.grid[1][1]).isEqualTo(8)
    }

    @Test
    fun `tryFlashing works in the middle of grid`() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")
        el.grid[4][4] = 10

        assertThat(el.tryFlashing(4, 4)).isTrue
        assertThat(el.grid[3][3]).isEqualTo(2)
        assertThat(el.grid[3][4]).isEqualTo(4)
        assertThat(el.grid[3][5]).isEqualTo(4)

        assertThat(el.grid[4][3]).isEqualTo(8)
        assertThat(el.grid[4][5]).isEqualTo(9)

        assertThat(el.grid[5][3]).isEqualTo(8)
        assertThat(el.grid[5][4]).isEqualTo(6)
        assertThat(el.grid[5][5]).isEqualTo(3)

        assertThat(el.grid[4][4]).isEqualTo(0)

        el.printGrid()
    }

    @Test
    fun `Do one step without flashes`() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")

        assertThat(el.doOneStep()).isEqualTo(0)

        val check = Eleventh(10)
        check.storeLines(
            listOf(
                "6594254334",
                "3856965822",
                "6375667284",
                "7252447257",
                "7468496589",
                "5278635756",
                "3287952832",
                "7993992245",
                "5957959665",
                "6394862637"
            )
        )

        assertThat(el.grid).isEqualTo(check.grid)
    }

    @Test
    fun `Do second step with flashes`() {
        val el = Eleventh(10)
        el.storeLines(
            listOf(
                "6594254334",
                "3856965822",
                "6375667284",
                "7252447257",
                "7468496589",
                "5278635756",
                "3287952832",
                "7993992245",
                "5957959665",
                "6394862637"
            )
        )

        assertThat(el.doOneStep()).isEqualTo(35)

        val check = Eleventh(10)
        check.storeLines(
            listOf(
                "8807476555",
                "5089087054",
                "8597889608",
                "8485769600",
                "8700908800",
                "6600088989",
                "6800005943",
                "0000007456",
                "9000000876",
                "8700006848"
            )
        )

        assertThat(el.grid).isEqualTo(check.grid)
    }

    @Test
    fun `Do 10 steps`() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")

        assertThat(el.doSteps(10)).isEqualTo(204)

        val check = Eleventh(10)
        check.storeLines(
            listOf(
                "0481112976",
                "0031112009",
                "0041112504",
                "0081111406",
                "0099111306",
                "0093511233",
                "0442361130",
                "5532252350",
                "0532250600",
                "0032240000"
            )
        )

        assertThat(el.grid).isEqualTo(check.grid)
    }

    @Test
    fun `Do 100 steps`() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")

        assertThat(el.doSteps(100)).isEqualTo(1656)

        val check = Eleventh(10)
        check.storeLines(
            listOf(
                "0397666866",
                "0749766918",
                "0053976933",
                "0004297822",
                "0004229892",
                "0053222877",
                "0532222966",
                "9322228966",
                "7922286866",
                "6789998766"
            )
        )

        assertThat(el.grid).isEqualTo(check.grid)
    }

    @Test
    fun `Do 100 steps of first part`() {
        val el = Eleventh(10)
        el.load("/11_first.txt")

        assertThat(el.doSteps(100)).isEqualTo(1669)
    }

    @Test
    fun `Find all flushing for intro`() {
        val el = Eleventh(10)
        el.load("/11_intro.txt")

        assertThat(el.findAllFlashing()).isEqualTo(195)
    }

    @Test
    fun `Find all flushing for real`() {
        val el = Eleventh(10)
        el.load("/11_first.txt")

        assertThat(el.findAllFlashing()).isEqualTo(351)
    }
}