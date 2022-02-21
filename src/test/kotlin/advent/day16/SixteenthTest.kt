package advent.day16

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@OptIn(ExperimentalUnsignedTypes::class)
internal class SixteenthTest {
    @Test
    fun `operations should work`() {
        assertThat("D2".hexToUInt()).isEqualTo(0b11010010.toUInt())
        val bits = 0b11010010111111100010100000000000.toUInt()

        assertThat(bits.takeFirstBits(3)).isEqualTo(0b110.toUInt())
        assertThat(bits.getBits(2, 2)).isEqualTo(0b01.toUInt())
        assertThatThrownBy { bits.getBits(28, 6) }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `operations on array in one block should work`() {
        val data = UIntArray(1) { 0b11010010111111100010100000000000.toUInt() }

        assertThat(data.getBits(0, 3)).isEqualTo(0b110.toUInt())
        assertThat(data.getBits(2, 2)).isEqualTo(0b01.toUInt())
        assertThatThrownBy { data.getBits(28, 6) }.isInstanceOf(IllegalStateException::class.java)
        assertThatThrownBy { data.getBits(0, 34) }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `operations on array in multiple blocks should work`() {
        val data = UIntArray(2)
        data[0] = 0b11010010111111100010100000000010.toUInt()
        data[1] = 0b11010010111111100010100000000000.toUInt()

        assertThat(data.getBits(32, 3)).isEqualTo(0b110.toUInt())
        assertThat(data.getBits(0, 32)).isEqualTo(data[0])
        assertThat(data.getBits(32, 32)).isEqualTo(data[1])

        assertThat(data.getBits(30, 4)).isEqualTo(0b1011.toUInt())
        assertThat(data.getBits(16, 32)).isEqualTo(0b101000000000101101001011111110.toUInt())

        assertThatThrownBy { data.getBits(60, 6) }.isInstanceOf(IllegalStateException::class.java)
        assertThatThrownBy { data.getBits(0, 34) }.isInstanceOf(IllegalStateException::class.java)
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
        val typeId = bites.getBits(3, 3)

        assertThat(version).isEqualTo(6.toUInt())
        assertThat(typeId).isEqualTo(4.toUInt())
    }

    @Test
    fun `decode example data`() {
        val s = Sixteenth()
        val arr = s.parse("D2FE28")

        Message.readHeader(arr, 0)
        val header = Header(6.toUInt(), 4.toUInt(), 6)
        assertThat(LiteralValue.readMessage(arr, 6, header))
            .isEqualTo(
                LiteralValue(
                    value = 2021.toUInt(),
                    bitCount = 24,
                    header = header
                )
            )

    }

    @Test
    fun `read message with Message`() {
        val s = Sixteenth()
        val arr = s.parse("D2FE28")
        val message = Message.readMessage(arr, 0)
        val header = Header(6.toUInt(), 4.toUInt(), 6)

        assertThat(message).isInstanceOf(LiteralValue::class.java)
        assertThat(message).isEqualTo(
            LiteralValue(
                value = 2021.toUInt(),
                bitCount = 24,
                header = header
            )
        )
    }
}