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

        assertThat(s.parse("020D7900").toList()).describedAs("Even hex number should be imported")
            .isEqualTo(
                uintArrayOf(
                    0b00000010000011010111100100000000.toUInt()
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
                    value = 2021.toULong(),
                    bitCount = 21,
                    header = header
                )
            )

    }

    @Test
    fun `parse LiteralValue bigger than 32 bits`() {
        val data = UIntArray(2)
        data[0] = 0b11010010111111101010110001100001.toUInt()
        data[1] = 0b11111000110001100011000000000000.toUInt()
        val message = Message.readMessage(data, 0)
        val literal = message.first() as LiteralValue

        assertThat(literal.value).isEqualTo(8680413139200.toULong())
    }

    @Test
    fun `read message with Message`() {
        val s = Sixteenth()
        val arr = s.parse("D2FE28")
        val message = Message.readMessage(arr, 0)
        val header = Header(6.toUInt(), 4.toUInt(), 6)

        assertThat(message.first()).isInstanceOf(LiteralValue::class.java)
        assertThat(message.first()).isEqualTo(
            LiteralValue(
                value = 2021.toULong(),
                bitCount = 21,
                header = header
            )
        )
    }

    @Test
    fun `read operator message with lengthBits set`() {
        val s = Sixteenth()
        val arr = s.parse("38006F45291200")
        val messages = Message.readMessage(arr, 0)

        assertThat(messages.first()).isInstanceOf(Operator::class.java)
        assertThat((messages.first() as Operator).bitCount).isEqualTo(22)
        assertThat(messages).hasSize(3)
        assertThat((messages[1] as LiteralValue).value).isEqualTo(10.toULong())
        assertThat((messages[2] as LiteralValue).value).isEqualTo(20.toULong())
        assertThat(messages.fold(0) { acc, it -> acc + it.bitCount }).isEqualTo(49)
        assertThat(arr.getBits(49, 7)).isEqualTo(0.toUInt())
    }

    @Test
    fun `read operator message with lengthNo set`() {
        val s = Sixteenth()
        val arr = s.parse("EE00D40C823060")
        val messages = Message.readMessage(arr, 0)

        assertThat(messages.first()).isInstanceOf(Operator::class.java)
        assertThat((messages.first() as Operator).bitCount).isEqualTo(18)
        assertThat(messages).hasSize(4)

        assertThat((messages[1] as LiteralValue).value).isEqualTo(1.toULong())
        assertThat((messages[2] as LiteralValue).value).isEqualTo(2.toULong())
        assertThat((messages[3] as LiteralValue).value).isEqualTo(3.toULong())
        assertThat(messages.fold(0) { acc, it -> acc + it.bitCount }).isEqualTo(51)
        assertThat(arr.getBits(51, 5)).isEqualTo(0.toUInt())
    }

    @Test
    fun `read operator message with operator inside`() {
        val s = Sixteenth()
        val arr = s.parse("8A004A801A8002F478")
        val messages = Message.readMessage(arr, 0)

        assertThat(messages[0]).isInstanceOf(Operator::class.java)
        assertThat(messages[1]).isInstanceOf(Operator::class.java)
        assertThat(messages[2]).isInstanceOf(Operator::class.java)
        assertThat(messages[3]).isInstanceOf(LiteralValue::class.java)

        assertThat(messages).hasSize(4)
        assertThat(messages.getVersionSum()).isEqualTo(16)
        assertThat(messages.fold(0) { acc, it -> acc + it.bitCount }).isEqualTo(69)
        assertThat(arr.getBits(72, 2)).isEqualTo(0.toUInt())
    }

    @Test
    fun `read operator message with subpackets`() {
        val s = Sixteenth()
        val arr = s.parse("620080001611562C8802118E34")
        val messages = Message.readMessage(arr, 0)

        assertThat(messages).hasSize(7)
        assertThat(messages.getVersionSum()).isEqualTo(12)
        assertThat(messages.fold(0) { acc, it -> acc + it.bitCount }).isEqualTo(102)
        assertThat(arr.getBits(102, 2)).isEqualTo(0.toUInt())
    }

    @Test
    fun `read operator message with subpackets - different lengths`() {
        val s = Sixteenth()
        val arr = s.parse("C0015000016115A2E0802F182340")
        val messages = Message.readMessage(arr, 0)

        assertThat(messages).hasSize(7)
        assertThat(messages.getVersionSum()).isEqualTo(23)
        assertThat(messages.fold(0) { acc, it -> acc + it.bitCount }).isEqualTo(106)
        assertThat(arr.getBits(106, 6)).isEqualTo(0.toUInt())
    }

    @Test
    fun `read 3 operator messages with subpackets`() {
        val s = Sixteenth()
        val arr = s.parse("A0016C880162017C3686B18A3D4780")
        val messages = Message.readMessage(arr, 0)

        assertThat(messages).hasSize(8)
        assertThat(messages.getVersionSum()).isEqualTo(31)
        assertThat(messages.fold(0) { acc, it -> acc + it.bitCount }).isEqualTo(113)
        assertThat(arr.getBits(113, 7)).isEqualTo(0.toUInt())
    }

    @Test
    fun `read input message`() {
        val s = Sixteenth()
        val arr = s.load("/16.txt")
        val messages = Message.readMessage(arr, 0)
        assertThat(messages.getVersionSum()).isEqualTo(906)
    }
}