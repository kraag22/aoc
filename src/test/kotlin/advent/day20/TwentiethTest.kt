package advent.day20

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TwentiethTest {
    @Test
    fun `try to parse example data`() {
        val t = Twentieth()
        t.extract("/20_example.txt")

        assertThat(t.enhancement).hasSize(512)
        assertThat(t.inputImage).hasSize(5)
        assertThat(t.inputImage.first()).hasSize(5)
    }

    @Test
    fun `getting points from inputImage works`() {
        val t = Twentieth()
        t.extract("/20_example.txt")

        assertThat(t.getInputPoint(3, 0)).isEqualTo('#')
        assertThat(t.getInputPoint(-1, 3)).isEqualTo('.')
        assertThat(t.getInputPoint(100, 3)).isEqualTo('.')
        assertThat(t.getInputPoint(2, -1)).isEqualTo('.')
        assertThat(t.getInputPoint(2, 100)).isEqualTo('.')
        assertThat(t.getInputPoint(0, 1)).isEqualTo('#')
        assertThat(t.getInputPoint(1, 2)).isEqualTo('#')
        assertThat(t.getInputPoint(1, 1)).isEqualTo('.')
    }

    @Test
    fun `computing enhancement index`() {
        val t = Twentieth()
        t.extract("/20_example.txt")

        assertThat(t.getEnhancementIndex(2, 2)).isEqualTo(34)
        assertThat(t.getEnhancementIndex(0, 0)).isEqualTo(18)
    }

    @Test
    fun `computing output point`() {
        val t = Twentieth()
        t.extract("/20_example.txt")

        assertThat(t.computeOutputPoint(2, 2)).isEqualTo('#')
        assertThat(t.computeOutputPoint(0, 0)).isEqualTo('.')
    }

    @Test
    fun `twice enhance example input image`() {
        val t = Twentieth()
        t.extract("/20_example.txt")

        val enhancedImage = t.enhanceImage()

        assertThat(enhancedImage[0]).isEqualTo(".##.##.".toList())
        assertThat(enhancedImage[1]).isEqualTo("#..#.#.".toList())
        assertThat(enhancedImage[2]).isEqualTo("##.#..#".toList())
        assertThat(enhancedImage[6]).isEqualTo("...#.#.".toList())

        t.inputImage = enhancedImage.toMutableList()
        val twiceEnhancedImage = t.enhanceImage()

        val expected = listOf(
            ".......#.",
            ".#..#.#..",
            "#.#...###",
            "#...##.#.",
            "#.....#.#",
            ".#.#####.",
            "..#.#####",
            "...##.##.",
            "....###.."
        )
        expected.forEachIndexed { index, s ->
            assertThat(twiceEnhancedImage.get(index)).isEqualTo(s.toList())
        }

        assertThat(twiceEnhancedImage.countHashes()).isEqualTo(35)
    }

    @Test
    fun `twice enhance works on example data`() {
        val t = Twentieth()
        t.extract("/20_example.txt")

        val enhancedImage = t.enhance(2)
        assertThat(enhancedImage.countHashes()).isEqualTo(35)
    }

    @Test
    fun `50x enhance example data`() {
        val t = Twentieth()
        t.extract("/20_example.txt")

        val enhancedImage = t.enhance(50)
        assertThat(enhancedImage.countHashes()).isEqualTo(3351)
    }

    @Test
    fun `compute hashes on enhanced input data`() {
        val t = Twentieth()
        t.extract("/20.txt")

        val enhancedImage = t.enhance(2)
        assertThat(enhancedImage.countHashes()).isEqualTo(5306)
    }

    @Test
    fun `50x enhance input data`() {
        val t = Twentieth()
        t.extract("/20.txt")

        val enhancedImage = t.enhance(50)
        assertThat(enhancedImage.countHashes()).isEqualTo(17497)
    }
}