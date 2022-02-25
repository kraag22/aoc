@file:OptIn(ExperimentalUnsignedTypes::class)

package advent.day16

import advent.Base
import kotlin.math.ceil

class Sixteenth : Base() {

    fun load(filename: String): UIntArray {
        val list = readData(filename)
        return parse(list.first())
    }

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

fun UInt.toPrint() = toString(2).padStart(32, '0')

fun UIntArray.getBits(at: Int, bitCount: Int): UInt {
    val blockSize = UInt.SIZE_BITS
    check(bitCount <= blockSize) { "bitCount is greater than size of UInt: $blockSize" }
    check(at + bitCount <= size * blockSize) { "Trying to get data outside range at: $at, bitCount: $bitCount" }

    val blockNo = at.floorDiv(blockSize)
    val blockAt = at.mod(blockSize)
    println("${this[blockNo].toPrint()} [$blockNo]")
    return if (blockAt + bitCount <= blockSize) {
        this[blockNo].getBits(blockAt, bitCount)
    } else {
        val otherBlockBitCount = (blockAt + bitCount).mod(blockSize)
        val firstPart = this[blockNo].getBits(blockAt, blockSize - blockAt)
        val secondPart = this[blockNo + 1].getBits(0, otherBlockBitCount)
        return (firstPart shl otherBlockBitCount) or secondPart
    }
}

fun List<Message>.getVersionSum() = sumOf { it.header.version }.toInt()

data class Header(val version: UInt, val typeId: UInt, val bitCount: Int = 6)

sealed class Message {
    abstract val bitCount: Int
    abstract val header: Header

    companion object {
        fun readMessage(data: UIntArray, basePosition: Int): List<Message> {
            var position = basePosition
            val header = readHeader(data, basePosition)
            position += header.bitCount
            return when (header.typeId) {
                4.toUInt() -> listOf(LiteralValue.readMessage(data, position, header))
                else -> Operator.readMessage(data, position, header)
            }
        }

        fun readHeader(data: UIntArray, position: Int): Header {
            val version = data.getBits(position, 3)
            val typeId = data.getBits(position + 3, 3)
            println("Header[$position], version:${version}, typeId: ${typeId}")
            return Header(version, typeId, 6)
        }
    }
}

data class LiteralValue(val value: ULong, override val bitCount: Int, override val header: Header) : Message() {
    companion object {
        fun readMessage(data: UIntArray, basePosition: Int, header: Header): LiteralValue {
            val mask = 0b1111.toUInt()
            var value = 0.toULong()
            var partsNo = 0

            while (true) {
                val part = data.getBits(basePosition + 5 * partsNo, 5)
                val continueBit = part.shr(4) and 0b1.toUInt()
                val partValue = part and mask
                value = value.shl(4) or partValue.toULong()
                partsNo++
                if (continueBit == 0.toUInt()) break
            }

            require(partsNo < 16) { "Maximum value is 64 bits, cannot store: ${(partsNo) * 4} bits" }

            val messageSize = header.bitCount + 5 * partsNo
            println("LiteralValue[${basePosition}] value:$value, size:$messageSize")

            return LiteralValue(
                value = value,
                bitCount = messageSize,
                header = header
            )
        }
    }
}

data class Operator(override val bitCount: Int, override val header: Header) : Message() {
    companion object {
        fun readMessage(data: UIntArray, basePosition: Int, header: Header): List<Message> {
            var position = basePosition
            var lengthTypeSize = 0
            val subPackets = mutableListOf<Message>()
            val lengthTypeId = data.getBits(position++, 1)
            if (lengthTypeId == 0.toUInt()) {
                val subPacketsMaxSizeBits = data.getBits(position, 15)
                println("Operator[$basePosition], subPacketBits: ${subPacketsMaxSizeBits.toString(2)} (${subPacketsMaxSizeBits.toInt()})")
                position += 15
                lengthTypeSize = 15
                var subPacketsSize = 0
                while (subPacketsSize < subPacketsMaxSizeBits.toInt()) {
                    val messages = Message.readMessage(data, position)
                    val newMessagesSize = messages.fold(0) { acc, it ->
                        acc + it.bitCount
                    }

                    subPacketsSize += newMessagesSize
                    position += newMessagesSize
                    subPackets += messages
                }
            } else {
                val subPacketsNo = data.getBits(position, 11)
                println("Operator[$basePosition], subPacketNo: ${subPacketsNo.toString(2)} (${subPacketsNo.toInt()})")

                position += 11
                lengthTypeSize = 11
                repeat(subPacketsNo.toInt()) {
                    val messages = Message.readMessage(data, position)
                    position += messages.fold(0) { acc, it ->
                        acc + it.bitCount
                    }
                    subPackets += messages
                }

            }
            return listOf(Operator(header.bitCount + 1 + lengthTypeSize, header)) + subPackets
        }
    }
}