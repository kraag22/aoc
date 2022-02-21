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

    fun readHeader(data: UIntArray, position: Int) {
        val version = data[0].getBits(position, 3)
        val typeId = data[0].getBits(position + 3, 3)
        println("version: ${version.toString(2)}, $version")
        println("typeId: ${typeId.toString(2)}, $typeId")

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
