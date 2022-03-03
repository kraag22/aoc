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
            "[[[[1,3],[5,3]],[[1,3],[8,[2,3]]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]"
        )
        for (number in numbers) {
            assertThat(e.parse(number).print()).isEqualTo(number)
        }
    }

    @Test
    fun `test levels`() {
        val e = Eighteenth()
        val numbers = listOf(
            Pair("[1,2]", 1),
            Pair("[9,[8,7]]", 2),
            Pair("[[1,9],[8,5]]", 2),
            Pair("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]", 4),
            Pair("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]", 4),
            Pair("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]", 4),
        )
        for (it in numbers) {
            assertThat(e.parse(it.first).getLevel())
                .describedAs("Failed for snailfish: ${it.first}")
                .isEqualTo(it.second)
        }
    }

    @Test
    fun `get exploding leaves`() {
        val e = Eighteenth()
        val numbers = listOf(
            Pair("[1,2]", null),
            Pair("[9,[8,7]]", null),
            Pair("[[1,9],[8,5]]", null),
            Pair("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]", null),
            Pair("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]", null),
            Pair("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]", null),
            Pair("[[[[[1,1],3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]", "[1,1]"),
            Pair("[[[[1,3],[5,3]],[[1,3],[8,[2,3]]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]", "[2,3]"),
            Pair("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", "[8,4]")
        )
        for (it in numbers) {
            assertThat(e.parse(it.first).getExploding()?.print())
                .describedAs("Failed for snailfish: ${it.first}")
                .isEqualTo(it.second)
        }
    }

    @Test
    fun `get non-existent neighbour on left of {1,1}`() {
        val e = Eighteenth()
        val fish = e.parse("[[[[[1,1],3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]")
        val exploding = fish.getExploding()
        assertThat(exploding!!.parent?.getNeighbour(goDown = true, goLeft = false, exploding)?.print()).isEqualTo("3")
        assertThat(
            exploding!!.parent?.getNeighbour(goDown = true, goLeft = true, exploding)?.print()
        ).isNull()
    }

    @Test
    fun `get neighbours of {2,3}`() {
        val e = Eighteenth()
        val fish = e.parse("[[[[1,3],[5,3]],[[1,3],[8,[2,3]]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]")
        val exploding = fish.getExploding()
        assertThat(
            exploding!!.parent?.getNeighbour(goDown = true, goLeft = false, exploding)?.print()
        ).isEqualTo("4")
        assertThat(
            exploding!!.parent?.getNeighbour(goDown = true, goLeft = true, exploding)?.print()
        ).isEqualTo("8")
    }

    @Test
    fun `get neighbours of {8,4}`() {
        val e = Eighteenth()
        val fish = e.parse("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]")
        val exploding = fish.getExploding()
        assertThat(
            exploding!!.parent?.getNeighbour(goDown = true, goLeft = false, exploding)?.print()
        ).isEqualTo("9")
        assertThat(
            exploding!!.parent?.getNeighbour(goDown = true, goLeft = true, exploding)?.print()
        ).isEqualTo("7")
    }

    @Test
    fun `test explosion 1`() {
        val e = Eighteenth()
        val fish = e.parse("[[[[[9,8],1],2],3],4]")
        fish.explode()
        assertThat(fish.print()).isEqualTo("[[[[0,9],2],3],4]")
    }

    @Test
    fun `test explosion 2`() {
        val e = Eighteenth()
        val fish = e.parse("[7,[6,[5,[4,[3,2]]]]]")
        fish.explode()
        assertThat(fish.print()).isEqualTo("[7,[6,[5,[7,0]]]]")
    }

    @Test
    fun `test explosion 3`() {
        val e = Eighteenth()
        val fish = e.parse("[[6,[5,[4,[3,2]]]],1]")
        fish.explode()
        assertThat(fish.print()).isEqualTo("[[6,[5,[7,0]]],3]")
    }

    @Test
    fun `test explosion 4`() {
        val e = Eighteenth()
        val fish = e.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
        fish.explode()
        assertThat(fish.print()).isEqualTo("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    }

    @Test
    fun `test explosion 5`() {
        val e = Eighteenth()
        val fish = e.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
        fish.explode()
        assertThat(fish.print()).isEqualTo("[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    }
}