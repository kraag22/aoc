package advent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SeventeenthTest {

    @Test
    fun getMinXVelocityTest() {
        val seven = Seventeenth(Pair(10, 20), Pair(-10, -5))
        assertThat(seven.getMaxVelocityForX()).isEqualTo(5)

        val seven2 = Seventeenth(Pair(5, 10), Pair(-10, -5))
        assertThat(seven2.getMaxVelocityForX()).isEqualTo(4)

        val seven3 = Seventeenth(Pair(40, 50), Pair(-10, -5))
        assertThat(seven3.getMaxVelocityForX()).isEqualTo(9)

        val seven4 = Seventeenth(Pair(79, 137), Pair(-10, -5))
        assertThat(seven4.getMaxVelocityForX()).isEqualTo(16)
    }

    @Test
    fun getMaxVelocityForYTest() {
        val seven = Seventeenth(Pair(10, 20), Pair(-10, -5))

        assertThat(
            seven.getMaxVelocityForY(seven.getMaxVelocityForX(), seven.targetY.first)
        ).isEqualTo(0)

        assertThat(
            seven.getMaxVelocityForY(seven.getMaxVelocityForX(), seven.targetY.second)
        ).isEqualTo(1)
    }

    @Test
    fun processHitsTest() {
        val seven = Seventeenth(Pair(20, 30), Pair(-10, -5))
        assertThat(seven.proccessHits()).isEqualTo(112)
    }

    @Test
    fun processHitsBigTest() {
        val seven = Seventeenth(Pair(79, 137), Pair(-176, -117))
        assertThat(seven.proccessHits()).isEqualTo(5844)
    }

    @Test
    fun processTest() {
        val seven = Seventeenth(Pair(20, 30), Pair(-10, -5))
        assertThat(seven.process()).isEqualTo(45)
    }

    @Test
    fun processBigTest() {
        val seven = Seventeenth(Pair(79, 137), Pair(-176, -117))
        assertThat(seven.process()).isEqualTo(15400)
    }

    @Test
    fun calculateMaxHeightTest() {
        val seven = Seventeenth(Pair(20, 30), Pair(-10, -5))
        assertThat(seven.calculateMaxHeight(6, 3)).isEqualTo(6)
        assertThat(seven.calculateMaxHeight(7, 2)).isEqualTo(3)
        assertThat(seven.calculateMaxHeight(9, 0)).isEqualTo(0)
        assertThat(seven.calculateMaxHeight(17, -4)).isEqualTo(-1)
        assertThat(seven.calculateMaxHeight(6, 9)).isEqualTo(45)
        assertThat(seven.calculateMaxHeight(7, 9)).isEqualTo(45)
        assertThat(seven.calculateMaxHeight(24, -9)).isEqualTo(0)
    }

    @Test
    fun outOfBoundsTest() {
        val seven = Seventeenth(Pair(20, 30), Pair(-10, -5))
        assertThat(seven.outOfBounds(31, -7)).isTrue
        assertThat(seven.outOfBounds(1, -11)).isTrue
        assertThat(seven.outOfBounds(1, 0)).isFalse
        assertThat(seven.outOfBounds(29, -7)).isFalse
    }

    @Test
    fun hitTest() {
        val seven = Seventeenth(Pair(20, 30), Pair(-10, -5))
        assertThat(seven.hit(21, -7)).isTrue
        assertThat(seven.hit(21, -10)).isTrue
        assertThat(seven.hit(21, -5)).isTrue
        assertThat(seven.hit(20, -7)).isTrue
        assertThat(seven.hit(30, -10)).isTrue
        assertThat(seven.hit(29, -5)).isTrue

        assertThat(seven.hit(19, -7)).isFalse
        assertThat(seven.hit(31, -10)).isFalse
        assertThat(seven.hit(-5, -5)).isFalse
        assertThat(seven.hit(21, -11)).isFalse
        assertThat(seven.hit(21, -3)).isFalse
        assertThat(seven.hit(21, 50)).isFalse

    }
}