package advent.day5

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FifthTest {
    @Test
    fun `test load example data`() {
        val f = Fifth()
        f.extract("/5_example.txt")

        assertThat(f.lines).hasSize(10)
    }

    @Test
    fun `test example data - only horizontal and vertical`() {
        val f = Fifth()
        f.extract("/5_example.txt")

        assertThat(f.compute(false)).isEqualTo(5)
    }

    @Test
    fun `test example data - full`() {
        val f = Fifth()
        f.extract("/5_example.txt")

        assertThat(f.compute(true)).isEqualTo(12)
    }

    @Test
    fun `compute only horizontal and vertical`() {
        val f = Fifth()
        f.extract("/5.txt")

        assertThat(f.compute(false)).isEqualTo(5280)
    }

    @Test
    fun `compute full`() {
        val f = Fifth()
        f.extract("/5.txt")

        assertThat(f.compute(true)).isEqualTo(16716)
    }

    @Test
    fun `compute parameters for line equation`() {
        assertThat(Pair(2, 0) - Pair(6, 4)).isEqualTo(Pair(-4, -4))

        val l = Line(Pair(6, 4), Pair(2, 0))
        assertThat(l.getParametersForEquation()).isEqualTo(Triple(-1, 1, 2))

        val horizontalLine = Line(Pair(6, 2), Pair(10, 2))
        assertThat(horizontalLine.getParametersForEquation()).isEqualTo(Triple(0, 1, -2))

        val verticalLine = Line(Pair(6, 8), Pair(6, 4))
        assertThat(verticalLine.getParametersForEquation()).isEqualTo(Triple(1, 0, -6))
    }

    @Test
    fun `compare equation for same lines`() {
        val l = Line(Pair(6, 4), Pair(2, 0))
        val l2 = Line(Pair(10, 8), Pair(-1, -3))

        assertThat(l.isSameAs(l2)).isTrue

        val l3 = Line(Pair(6, 4), Pair(2, 4))
        val l4 = Line(Pair(10, 4), Pair(-10, 4))

        assertThat(l3.isSameAs(l4)).isTrue

        val l5 = Line(Pair(4, 4), Pair(8, 8))
        val l6 = Line(Pair(-91, -91), Pair(-61, -61))
        assertThat(l5.isSameAs(l6)).isTrue
    }

    @Test
    fun `compute intersection of two lines`() {
        val l = Line(Pair(0, 0), Pair(-1, 1))
        val l2 = Line(Pair(2, 0), Pair(3, 1))
        assertThat(l.getIntersectionPoint(l2)).isEqualTo(Pair(1, -1))
    }

    @Test
    fun `compute intersection of two lines without one`() {
        val l = Line(Pair(0, 9), Pair(5, 9))
        val l2 = Line(Pair(7, 0), Pair(7, 4))
        assertThat(l.getIntersectionPoint(l2)).isEqualTo(Pair(7, 9))
    }

    @Test
    fun `compute intersection bugged lines`() {
        val l = Line(Pair(0, 9), Pair(9, 9))
        val l2 = Line(Pair(8, 8), Pair(10, 10))
        assertThat(l.getIntersectionPoint(l2)).isEqualTo(Pair(9, 9))
    }

    @Test
    fun `compute intersection bugged lines exchanged`() {
        val l = Line(Pair(8, 8), Pair(10, 10))
        val l2 = Line(Pair(0, 9), Pair(9, 9))
        assertThat(l.getIntersectionPoint(l2)).isEqualTo(Pair(9, 9))
    }

    @Test
    fun `compute intersection of two vertical lines without one`() {
        val l = Line(Pair(3, 0), Pair(3, 2))
        val l2 = Line(Pair(5, 0), Pair(5, 2))
        assertThat(l.getIntersectionPoint(l2)).isEqualTo(null)
    }

    @Test
    fun `compute intersection of two horizontal lines without one`() {
        val l = Line(Pair(1, 6), Pair(3, 6))
        val l2 = Line(Pair(2, 4), Pair(5, 4))
        assertThat(l.getIntersectionPoint(l2)).isEqualTo(null)
    }

    @Test
    fun `containsPoint should work`() {
        val l = Line(Pair(1, 6), Pair(3, 4))

        assertThat(l.containsPoint(Pair(0, 7))).isFalse
        assertThat(l.containsPoint(Pair(0, 6))).isFalse
        assertThat(l.containsPoint(Pair(4, 3))).isFalse
        assertThat(l.containsPoint(Pair(6, 2))).isFalse

        assertThat(l.containsPoint(Pair(1, 6))).isTrue
        assertThat(l.containsPoint(Pair(2, 5))).isTrue
        assertThat(l.containsPoint(Pair(3, 4))).isTrue

        val l2 = Line(Pair(3, 0), Pair(3, 2))

        assertThat(l.containsPoint(Pair(3, 60))).isFalse
        assertThat(l2.containsPoint(Pair(3, 2))).isTrue
        assertThat(l2.containsPoint(Pair(3, 1))).isTrue
        assertThat(l2.containsPoint(Pair(3, 0))).isTrue
        assertThat(l2.containsPoint(Pair(3, -1))).isFalse
        assertThat(l2.containsPoint(Pair(33, -1))).isFalse
    }

    @Test
    fun `overlappingEdges should work`() {
        assertThat(
            Line(Pair(6, 4), Pair(2, 0)).getOverlappingEdges(Line(Pair(10, 8), Pair(-1, -3)))
        ).isEqualTo(setOf(Pair(6, 4), Pair(2, 0)))

        assertThat(
            Line(Pair(6, 4), Pair(2, 0)).getOverlappingEdges(Line(Pair(2, 0), Pair(-1, -3)))
        ).isEqualTo(setOf(Pair(2, 0)))

        assertThat(
            Line(Pair(6, 4), Pair(17, 15)).getOverlappingEdges(Line(Pair(10, 8), Pair(-1, -3)))
        ).isEqualTo(setOf(Pair(6, 4), Pair(10, 8)))

        assertThat(
            Line(Pair(2, 0), Pair(-2, 0)).getOverlappingEdges(Line(Pair(10, 8), Pair(-1, -3)))
        ).isEqualTo(emptySet<Pair<Int, Int>>())
    }

    @Test
    fun `overlapping should work for diagonal lines`() {
        val l1 = Line(Pair(2, 2), Pair(4, 4))
        val l2 = Line(Pair(3, 10), Pair(3, -5))

        assertThat(l1.getAllPoints(l2)).isEqualTo(setOf(Pair(3, 3)))

        val l3 = Line(Pair(2, 2), Pair(4, 4))
        val l4 = Line(Pair(4, 4), Pair(5, 5))

        assertThat(l3.getAllPoints(l4)).isEqualTo(setOf(Pair(4, 4)))
    }

    @Test
    fun `overlappingPoints should work`() {
        assertThat(
            Line(Pair(6, 4), Pair(2, 0)).getOverlappingPoints(setOf(Pair(2, 0)))
        ).isEqualTo(setOf(Pair(2, 0)))

        assertThat(
            Line(Pair(6, 4), Pair(2, 0)).getOverlappingPoints(setOf(Pair(6, 4), Pair(10, 8)))
        ).isEqualTo(setOf(Pair(6, 4), Pair(7, 5), Pair(8, 6), Pair(9, 7), Pair(10, 8)))

        assertThat(
            Line(Pair(6, 4), Pair(2, 0)).getOverlappingPoints(setOf(Pair(10, 8), Pair(6, 4)))
        ).isEqualTo(setOf(Pair(6, 4), Pair(7, 5), Pair(8, 6), Pair(9, 7), Pair(10, 8)))
    }

    @Test
    fun `getAllPoints should work`() {
        assertThat(
            Line(Pair(6, 4), Pair(2, 0)).getAllPoints(Line(Pair(10, 8), Pair(-1, -3)))
        ).isEqualTo(setOf(Pair(2, 0), Pair(3, 1), Pair(4, 2), Pair(5, 3), Pair(6, 4)))

        val l = Line(Pair(0, 0), Pair(-1, 1))
        val l2 = Line(Pair(2, 0), Pair(3, 1))

        assertThat(l.getAllPoints(l2)).isEmpty()
    }

    @Test
    fun `getAllPoints should work for these lines`() {
        val l = Line(Pair(0, 9), Pair(5, 9))
        val l2 = Line(Pair(7, 0), Pair(7, 4))

        assertThat(l.getAllPoints(l2)).isEmpty()
    }

    @Test
    fun `getAllPoints should work for horizontal and diagonal`() {
        val l = Line(Pair(0, 9), Pair(9, 9))
        val l2 = Line(Pair(8, 8), Pair(10, 10))

        assertThat(l.getAllPoints(l2)).isEqualTo(setOf(Pair(9, 9)))
    }
}