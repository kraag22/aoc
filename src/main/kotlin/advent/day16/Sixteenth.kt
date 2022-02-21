package advent.day16

import advent.Base
import kotlin.math.ceil

@OptIn(ExperimentalUnsignedTypes::class)
class Sixteenth : Base() {

    fun parse(hex: String): UIntArray {
        val size = ceil(hex.length / 8f).toInt()
        val array = UIntArray(size)
        hex.windowed(8, 8, true).forEachIndexed { index, pair ->
            array[index] = pair.padEnd(8, '0').hexToUInt()
        }
        return array
    }

}

fun String.hexToUInt() = toUInt(16)

fun UInt.takeFirstBits(bitCount: Int) = this shr (32 - bitCount)
fun UInt.getBits(at: Int, bitCount: Int): UInt {
    check(at + bitCount <= UInt.SIZE_BITS) { "Cannot access bits outside UInt at: $at, bitCount: $bitCount" }
    val shifted = this shl at
    return shifted.takeFirstBits(bitCount)
}

fun UIntArray.getBits(at: Int, bitCount: Int): UInt {
    val blockSize = UInt.SIZE_BITS
    check(bitCount <= blockSize) { "bitCount is greater than size of UInt: $blockSize" }
    check(at + bitCount <= size * blockSize) { "Trying to get data outside range at: $at, bitCount: $bitCount" }

    val blockNo = at.floorDiv(blockSize)
    val blockAt = at.mod(blockSize)

    return if (blockAt + bitCount <= blockSize) {
        this[blockNo].getBits(blockAt, bitCount)
    } else {
        val otherBlockBitCount = (blockAt + bitCount).mod(blockSize)
        val firstPart = this[blockNo].getBits(blockAt, blockSize - blockAt)
        val secondPart = this[blockNo + 1].getBits(0, otherBlockBitCount)
        return (firstPart shl otherBlockBitCount) or secondPart
    }
}

data class Header(val version: UInt, val typeId: UInt, val bitCount: Int = 6)

open class Message {

    companion object {
        fun readMessage(data: UIntArray, basePosition: Int): Message {
            var position = basePosition
            val header = readHeader(data, basePosition)
            position += header.bitCount
            return when (header.typeId) {
                4.toUInt() -> LiteralValue.readMessage(data, position, header)
                else -> TODO()
            }
        }

        fun readHeader(data: UIntArray, position: Int): Header {
            val version = data[0].getBits(position, 3)
            val typeId = data[0].getBits(position + 3, 3)
            println("version: ${version.toString(2)}, $version")
            println("typeId: ${typeId.toString(2)}, $typeId")
            return Header(version, typeId, 6)
        }
    }
}

data class LiteralValue(val value: UInt, val bitCount: Int, val header: Header) : Message() {
    companion object {
        fun readMessage(data: UIntArray, basePosition: Int, header: Header): LiteralValue {
            val mask = 0b1111.toUInt()
            var value = 0.toUInt()
            var partsNo = 0

            while (true) {
                val part = data.getBits(basePosition + 5 * partsNo, 5)
                val continueBit = part.shr(4) and 0b1.toUInt()
                val partValue = part and mask
                value = value.shl(4) or partValue
                if (continueBit == 0.toUInt()) break
                partsNo++
            }

            val messageSize = header.bitCount + basePosition + 5 * partsNo
            val unusedBits = (messageSize).mod(4)
            return LiteralValue(
                value = value,
                bitCount = messageSize + unusedBits,
                header = header
            )
        }
    }
}