package advent.day21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TwentyonethTest {
    @Test
    fun `test deterministic die`() {
        val die = DeterministicDie()
        for (i in (1..100)) {
            assertThat(
                die.roll()
            ).isEqualTo(i)
        }

        assertThat(die.roll()).isEqualTo(1)
        assertThat(die.roll3times()).isEqualTo(9)
    }

    @Test
    fun `get position should work`() {
        val die = DeterministicDie()

        assertThat(Twentyoneth.getNewPosition(Player(1, 4), die))
            .isEqualTo(10)

        assertThat(Twentyoneth.getNewPosition(Player(1, 10), die))
            .isEqualTo(5)

    }

    @Test
    fun `try example play`() {
        val die = DeterministicDie()

        val players = mutableListOf<Player>(
            Player(1, 4),
            Player(2, 8)
        )

        assertThat(Twentyoneth.play(die, players)).isEqualTo(739785)
    }

    @Test
    fun `try first part puzzle play`() {
        val die = DeterministicDie()

        val players = mutableListOf<Player>(
            Player(1, 7),
            Player(2, 8)
        )

        assertThat(Twentyoneth.play(die, players)).isEqualTo(556206)
    }
}