package advent.day21

import advent.Base

const val END_SCORE = 1000
const val PLAN_POSITIONS = 10

class Twentyoneth : Base() {

    companion object {
        fun play(die: IDie, players: MutableList<Player>): Long {
            while (!players.reachedEndScore()) {
                val player = players.removeFirst()
                val newPosition = getNewPosition(player, die)
                player.score += newPosition
                player.position = newPosition
                players.add(player)
            }
            return die.rolls * players.first().score
        }

        fun getNewPosition(player: Player, die: IDie): Int {
            (player.position + die.roll3times()).let {
                val modulo = it % PLAN_POSITIONS
                return if (modulo == 0) {
                    PLAN_POSITIONS
                } else {
                    modulo
                }
            }
        }
    }
}

private fun List<Player>.reachedEndScore(): Boolean {
    return any { it.score >= END_SCORE }
}

data class Player(val name: Int, var position: Int = 0, var score: Int = 0)

interface IDie {
    var rolls: Long
    fun roll(): Int
    fun roll3times(): Int
}

class DeterministicDie : IDie {
    override var rolls: Long = 0L
    var nextRoll: Int = 1
    private val maxRoll = 100

    override fun roll(): Int {
        rolls++
        val rolled = nextRoll
        if (nextRoll == maxRoll) {
            nextRoll = 1
        } else {
            nextRoll++
        }
        return rolled
    }

    override fun roll3times(): Int {
        return roll() + roll() + roll()
    }
}