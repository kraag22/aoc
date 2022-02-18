package advent.day16

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SixteenthTest {
    @Test
    fun `operations should work`() {
        assertThat("D2".hexToUInt()).isEqualTo(0b11010010.toUInt())
        val bites = 0b11010010111111100010100000000000.toUInt()

        assertThat(bites.takeFirstBits(3)).isEqualTo(0b110.toUInt())
        assertThat(bites.getBites(2, 2)).isEqualTo(0b01.toUInt())
    }

    @Test
    fun `parsing hex is stored to short array`() {
        val s = Sixteenth()

        assertThat(s.parse("D2FE28").toList()).describedAs("Even hex number should be imported")
            .isEqualTo(
                uintArrayOf(
                    0b11010010111111100010100000000000.toUInt()
                ).toList()
            )

        assertThat(s.parse("D2FE2").toList()).describedAs("Odd hex number has to be filled with zeroes")
            .isEqualTo(
                uintArrayOf(
                    0b11010010111111100010000000000000.toUInt()
                ).toList()
            )
    }

    @Test
    fun `manually decode header`() {
        val s = Sixteenth()
        val bites = s.parse("D2FE28").first()
        val version = bites.takeFirstBits(3)
        val typeId = bites.getBites(3, 3)

        assertThat(version).isEqualTo(6.toUInt())
        assertThat(typeId).isEqualTo(4.toUInt())
    }
}