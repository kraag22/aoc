package advent.day19

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.jupiter.api.Test

internal class NineteenthTest {
    @Test
    fun `try to load example data`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")

        assertThat(n.scanners.keys).hasSize(5)
        assertThat(n.scanners[0]).hasSize(25)
        assertThat(n.scanners[1]).hasSize(25)
        assertThat(n.scanners[2]).hasSize(26)
        assertThat(n.scanners[3]).hasSize(25)
        assertThat(n.scanners[4]).hasSize(26)
    }

    @Test
    fun `compute distance between two points`() {
        assertThat(
            Triple(0, 0, 0) distance Triple(1, 0, 0)
        ).isCloseTo(1.0, offset(0.0001))

        assertThat(
            Triple(1, 1, 1) distance Triple(1, 1, -1)
        ).isCloseTo(2.0, offset(0.0001))
    }

    @Test
    fun `compute distances between points for scanned area`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")

        assertThat(
            n.computeDistances(0)
        ).hasSize(25)
    }

    @Test
    fun `Find similarities in two scanned areas`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")
        val origDistances = n.computeDistances(0)
        val distances = n.computeDistances(1)

        var sum = 0
        for (i in (0..24)) {
            if (n.findSimilarity(i, origDistances, distances) != null) {
                sum++
            }
        }
        assertThat(sum).isEqualTo(12)
    }

    @Test
    fun `Find similar points in two scanned areas`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")

        val points = n.findSimilarPoints(0, 1)

        assertThat(points).hasSize(12)

        val screenZeroPoints = setOf(
            Triple(-618, -824, -621),
            Triple(-537, -823, -458),
            Triple(-447, -329, 318),
            Triple(404, -588, -901),
            Triple(544, -627, -890),
            Triple(528, -643, 409),
            Triple(-661, -816, -575),
            Triple(390, -675, -793),
            Triple(423, -701, 434),
            Triple(-345, -311, 381),
            Triple(459, -707, 401),
            Triple(-485, -357, 347)
        )

        assertThat(points.map { it.first }.toSet() - screenZeroPoints)
            .isEmpty()

        val screenOnePoints = setOf(
            Triple(686, 422, 578),
            Triple(605, 423, 415),
            Triple(515, 917, -361),
            Triple(-336, 658, 858),
            Triple(-476, 619, 847),
            Triple(-460, 603, -452),
            Triple(729, 430, 532),
            Triple(-322, 571, 750),
            Triple(-355, 545, -477),
            Triple(413, 935, -424),
            Triple(-391, 539, -444),
            Triple(553, 889, -390)
        )

        assertThat(points.map { it.second }.toSet() - screenOnePoints)
            .isEmpty()
    }

    @Test
    fun `Get modifications for X axis`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")

        val points = n.findSimilarPoints(0, 1)
        val base = points.map { it.first.first }
        val other = points.map { it.second.first }

        val axisModification = getAxisModificationFor(base, other)
        assertThat(axisModification).isEqualTo(
            AxisModification(
                68,
                -1,
                ScannerPerspectiveChange.NEGATIVE
            )
        )

        assertThat(
            other.map {
                axisModification!!.perspective.apply(it, axisModification.shift)
            }
        ).isEqualTo(base)

    }

    @Test
    fun `get modifications for difficult case`() {
        val basePoints = setOf(
            Triple(528, -643, 409),
            Triple(-485, -357, 347),
            Triple(-345, -311, 381),
            Triple(-447, -329, 318),
            Triple(423, -701, 434),
            Triple(459, -707, 401),
            Triple(408, -1815, 803),
            Triple(534, -1912, 768),
            Triple(-635, -1737, 486),
            Triple(432, -2009, 850),
            Triple(-739, -1745, 668),
            Triple(-687, -1600, 576)
        )

        val screen4Points = setOf(
            Triple(-652, -548, -490),
            Triple(-714, 465, -776),
            Triple(-680, 325, -822),
            Triple(-743, 427, -804),
            Triple(-627, -443, -432),
            Triple(-660, -479, -426),
            Triple(-258, -428, 682),
            Triple(-293, -554, 779),
            Triple(-575, 615, 604),
            Triple(-211, -452, 876),
            Triple(-393, 719, 612),
            Triple(-485, 667, 467)
        )

        val baseX = basePoints.map { it.first }
        val baseY = basePoints.map { it.second }
        val baseZ = basePoints.map { it.third }
        val otherX = screen4Points.map { it.first }
        val others = listOf(
            screen4Points.map { it.first },
            screen4Points.map { it.second },
            screen4Points.map { it.third }
        )

        assertThat(getAxisModificationFor(baseX, otherX)).isNull()
        assertThat(getAxisModificationWithOrderFor(baseX, others)).isEqualTo(
            AxisModification(-20, 1, ScannerPerspectiveChange.NEGATIVE)
        )
        assertThat(getAxisModificationWithOrderFor(baseY, others)).isEqualTo(
            AxisModification(-1133, 2, ScannerPerspectiveChange.NEGATIVE)
        )
        assertThat(getAxisModificationWithOrderFor(baseZ, others)).isEqualTo(
            AxisModification(1061, 0, ScannerPerspectiveChange.POSITIVE)
        )

    }

    @Test
    fun `Get modifications for points`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")

        val points = n.findSimilarPoints(0, 1)

        val modification = n.getModificationFor(points)
        assertThat(modification)
            .isEqualTo(
                Triple(
                    AxisModification(68, 0, ScannerPerspectiveChange.NEGATIVE),
                    AxisModification(-1246, 1, ScannerPerspectiveChange.POSITIVE),
                    AxisModification(-43, 2, ScannerPerspectiveChange.NEGATIVE)
                )
            )

        val modifiedScanner1Points = points.map { it.second }.map { it.modify(modification) }

        assertThat(points.map { it.first })
            .isEqualTo(modifiedScanner1Points)
    }

    @Test
    fun `try getting modifications for different scan areas`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")

        assertThat(n.getModificationFor(0, 1)).isEqualTo(
            Triple(
                AxisModification(68, 0, ScannerPerspectiveChange.NEGATIVE),
                AxisModification(-1246, 1, ScannerPerspectiveChange.POSITIVE),
                AxisModification(-43, 2, ScannerPerspectiveChange.NEGATIVE)
            )
        )

        assertThat(n.getModificationFor(0, 4)).isNull()
    }

    @Test
    fun `add area with similar points to base`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")

        assertThat(n.scanners[0]).hasSize(25)
        assertThat(n.scanners[1]).hasSize(25)
        assertThat(n.tryToAddScannerArea(0, 1)).isTrue
        assertThat(n.scanners[0]).hasSize(38)
    }

    @Test
    fun `join whole space together on example data`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")
        n.joinAreasTogether()
        assertThat(n.scanners.getValue(0)).hasSize(79)
    }

    @Test
    fun `join whole space together on input data`() {
        val n = Nineteenth()
        n.extract("/19.txt")
        n.joinAreasTogether()
        assertThat(n.scanners.getValue(0)).hasSize(398)
        assertThat(n.getLargestManhattanDistance()).isEqualTo(10965)
    }

    @Test
    fun `get Manhattan distances between two points`() {
        val result = Triple(1105, -1205, 1229)
            .manhattanDistanceTo(
                Triple(-92, -2380, -20)
            )
        assertThat(result).isEqualTo(3621)
    }

    @Test
    fun `get max manhattan distance on example data`() {
        val n = Nineteenth()
        n.extract("/19_example.txt")
        n.joinAreasTogether()
        assertThat(n.getLargestManhattanDistance()).isEqualTo(3621)
    }

}