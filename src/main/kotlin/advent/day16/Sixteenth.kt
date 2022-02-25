@file:OptIn(ExperimentalUnsignedTypes::class)

package advent.day16

import advent.Base
import java.math.BigInteger
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

sealed class Message {
    abstract val bitCount: Int
    abstract val header: Header
    abstract fun getSize(): Int
    abstract fun getVersionSum(): Int
    abstract fun getValue(): BigInteger

    companion object {
        fun readMessage(data: UIntArray, basePosition: Int): Message {
            var position = basePosition
            val header = readHeader(data, basePosition)
            position += header.bitCount
            return when (header.typeId) {
                4.toUInt() -> LiteralValue.readMessage(data, position, header)
                else -> Operator.readMessage(data, position, header)
            }
        }

        fun readHeader(data: UIntArray, position: Int): Header {
            val version = data.getBits(position, 3)
            val typeId = data.getBits(position + 3, 3)
            return Header(version, typeId, 6)
        }
    }
}

data class LiteralValue(val value: ULong, override val bitCount: Int, override val header: Header) : Message() {
    override fun getSize(): Int {
        return bitCount
    }

    override fun getVersionSum(): Int {
        return header.version.toInt()
    }

    override fun getValue(): BigInteger {
        return value.toLong().toBigInteger()
    }

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

            return LiteralValue(
                value = value,
                bitCount = messageSize,
                header = header
            )
        }
    }
}

data class Operator(override val bitCount: Int, override val header: Header, val subPackets: List<Message>) :
    Message() {

    override fun getSize(): Int {
        return bitCount + subPackets.sumOf { it.getSize() }
    }

    override fun getVersionSum(): Int {
        return (header.version.toInt() + subPackets.sumOf { it.getVersionSum() })
    }

    override fun getValue(): BigInteger {
        return when (header.typeId.toInt()) {
            0 -> {
                subPackets.sumOf { it.getValue() }
            }
            1 -> {
                subPackets.fold(BigInteger.ONE) { acc, message -> acc * message.getValue() }
            }
            2 -> {
                subPackets.minOf { it.getValue() }
            }
            3 -> {
                subPackets.maxOf { it.getValue() }
            }
            5 -> {
                check(subPackets.size == 2) { "5 operation - has to have only two subPackets" }
                if (subPackets.first().getValue() > subPackets.last().getValue()) BigInteger.ONE else BigInteger.ZERO
            }
            6 -> {
                check(subPackets.size == 2) { "6 operation - has to have only two subPackets" }
                if (subPackets.first().getValue() < subPackets.last().getValue()) BigInteger.ONE else BigInteger.ZERO
            }
            7 -> {
                check(subPackets.size == 2) { "7 operation - has to have only two subPackets" }
                if (subPackets.first().getValue() == subPackets.last().getValue()) BigInteger.ONE else BigInteger.ZERO
            }
            else -> throw Exception("Unimplemented method ${header.typeId}")
        }
    }

    companion object {
        fun readMessage(data: UIntArray, basePosition: Int, header: Header): Message {
            var position = basePosition
            var lengthTypeSize = 0
            val subPackets = mutableListOf<Message>()
            val lengthTypeId = data.getBits(position++, 1)
            if (lengthTypeId == 0.toUInt()) {
                val subPacketsMaxSizeBits = data.getBits(position, 15)
                position += 15
                lengthTypeSize = 15
                var subPacketsSize = 0
                while (subPacketsSize < subPacketsMaxSizeBits.toInt()) {
                    val message = Message.readMessage(data, position)
                    val newMessagesSize = message.getSize()

                    subPacketsSize += newMessagesSize
                    position += newMessagesSize
                    subPackets += message
                }
            } else {
                val subPacketsNo = data.getBits(position, 11)
                position += 11
                lengthTypeSize = 11
                repeat(subPacketsNo.toInt()) {
                    val message = Message.readMessage(data, position)
                    position += message.getSize()
                    subPackets += message
                }

            }
            return Operator(header.bitCount + 1 + lengthTypeSize, header, subPackets)
        }
    }
}