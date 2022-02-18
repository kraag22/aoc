package advent.day16

import advent.Base
import kotlin.math.ceil

class Sixteenth : Base() {

    @kotlin.ExperimentalUnsignedTypes
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
fun UInt.getBites(at: Int, bitCount: Int): UInt {
    val shifted = this shl at
    return shifted.takeFirstBits(bitCount)
}
