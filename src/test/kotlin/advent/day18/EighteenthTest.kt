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

    @Test
    fun `test get singles to split 1`() {
        val e = Eighteenth()
        val fish = e.parse("[[3,[2,[8,0]]],[9,[15,[4,[3,22]]]]]")
        assertThat(fish.getSplitting()?.print()).isEqualTo("15")
    }

    @Test
    fun `test get singles to split 2`() {
        val e = Eighteenth()
        val fish = e.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
        assertThat(fish.getSplitting()?.print()).isNull()
    }

    @Test
    fun `test get singles to split 3`() {
        val e = Eighteenth()
        val fish = e.parse("[[23,[2,[8,0]]],[9,[15,[4,[3,22]]]]]")
        assertThat(fish.getSplitting()?.print()).isEqualTo("23")
    }

    @Test
    fun `do split 1`() {
        val e = Eighteenth()
        val fish = e.parse("[[23,[2,[8,0]]],[9,[15,[4,[3,22]]]]]")
        fish.split()
        assertThat(fish.print()).isEqualTo("[[[11,12],[2,[8,0]]],[9,[15,[4,[3,22]]]]]")
    }

    @Test
    fun `do split 2`() {
        val e = Eighteenth()
        val fish = e.parse("[[3,[2,[8,0]]],[9,[15,[4,[3,22]]]]]")
        fish.split()
        assertThat(fish.print()).isEqualTo("[[3,[2,[8,0]]],[9,[[7,8],[4,[3,22]]]]]")
    }

    @Test
    fun `do split 3`() {
        val e = Eighteenth()
        val fish = e.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
        assertThat(fish.split()).isFalse
        assertThat(fish.print()).isEqualTo("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    }

    @Test
    fun `do reduce`() {
        val e = Eighteenth()
        val fish = e.parse("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")
        fish.reduce()
        assertThat(fish.print()).isEqualTo("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
    }

    @Test
    fun `process example 1`() {
        val e = Eighteenth()
        e.fishes.add(e.parse("[1,1]"))
        e.fishes.add(e.parse("[2,2]"))
        e.fishes.add(e.parse("[3,3]"))
        e.fishes.add(e.parse("[4,4]"))

        assertThat(e.process().print())
            .isEqualTo("[[[[1,1],[2,2]],[3,3]],[4,4]]")
    }

    @Test
    fun `process example 2`() {
        val e = Eighteenth()
        e.fishes.add(e.parse("[1,1]"))
        e.fishes.add(e.parse("[2,2]"))
        e.fishes.add(e.parse("[3,3]"))
        e.fishes.add(e.parse("[4,4]"))
        e.fishes.add(e.parse("[5,5]"))

        assertThat(e.process().print())
            .isEqualTo("[[[[3,0],[5,3]],[4,4]],[5,5]]")
    }

    @Test
    fun `process example 3`() {
        val e = Eighteenth()
        e.fishes.add(e.parse("[1,1]"))
        e.fishes.add(e.parse("[2,2]"))
        e.fishes.add(e.parse("[3,3]"))
        e.fishes.add(e.parse("[4,4]"))
        e.fishes.add(e.parse("[5,5]"))
        e.fishes.add(e.parse("[6,6]"))

        assertThat(e.process().print())
            .isEqualTo("[[[[5,0],[7,4]],[5,5]],[6,6]]")
    }

    @Test
    fun `process example 4`() {
        val e = Eighteenth()
        e.fishes.add(e.parse("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"))
        e.fishes.add(e.parse("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"))

        assertThat(e.process().print())
            .isEqualTo("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]")
    }

    @Test
    fun `process example data 2`() {
        val e = Eighteenth()
        e.extract("/18_example2.txt")

        val result = e.process()
        assertThat(result.print())
            .isEqualTo("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
        assertThat(result.getMagnitude()).isEqualTo(3488)
    }

    @Test
    fun `process example data`() {
        val e = Eighteenth()
        e.extract("/18_example.txt")

        val result = e.process()
        assertThat(result.print())
            .isEqualTo("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]")
        assertThat(result.getMagnitude()).isEqualTo(4140)
    }

    @Test
    fun `test magnitude`() {
        val e = Eighteenth()

        assertThat(e.parse("[9,1]").getMagnitude()).isEqualTo(29)
        assertThat(e.parse("[1,9]").getMagnitude()).isEqualTo(21)
        assertThat(e.parse("[[9,1],[1,9]]").getMagnitude()).isEqualTo(129)
        assertThat(e.parse("[[1,2],[[3,4],5]]").getMagnitude()).isEqualTo(143)
        assertThat(e.parse("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").getMagnitude()).isEqualTo(1384)
        assertThat(e.parse("[[[[1,1],[2,2]],[3,3]],[4,4]]").getMagnitude()).isEqualTo(445)
        assertThat(e.parse("[[[[3,0],[5,3]],[4,4]],[5,5]]").getMagnitude()).isEqualTo(791)
        assertThat(e.parse("[[[[5,0],[7,4]],[5,5]],[6,6]]").getMagnitude()).isEqualTo(1137)
        assertThat(e.parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").getMagnitude()).isEqualTo(3488)
    }

    @Test
    fun `process input data`() {
        val e = Eighteenth()
        e.extract("/18.txt")

        val result = e.process()
        assertThat(result.print())
            .isEqualTo("[[[[7,0],[7,7]],[[7,7],[7,7]]],[[[7,7],[8,7]],[[7,7],[7,6]]]]")
        assertThat(result.getMagnitude()).isEqualTo(4017)
    }

    @Test
    fun `process pairs on  example data 2`() {
        val e = Eighteenth()
        e.extract("/18_example.txt")

        val result = e.processPairs()

        assertThat(result).isEqualTo(3993)
    }

    @Test
    fun `process pairs on input data`() {
        val e = Eighteenth()
        e.extract("/18.txt")

        val result = e.processPairs()

        assertThat(result).isEqualTo(4583)
    }
}