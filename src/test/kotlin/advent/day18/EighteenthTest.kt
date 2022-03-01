package advent.day18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EighteenthTest {
    @Test
    fun `parse numbers`() {
        val e = Eighteenth()
        val numbers = listOf(
            "[1,2]",
            "[9,[8,7]]",
            "[[1,9],[8,5]]",
            "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
            "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
            "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
        )
        for (number in numbers) {
            assertThat(e.parse(number).print()).isEqualTo(number)
        }


    }
}