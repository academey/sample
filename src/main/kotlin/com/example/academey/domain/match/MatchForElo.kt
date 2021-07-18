package com.example.academey.domain.match

/**
 * Represents a match between two players.
 *
 */
class MatchForElo<E>(
    firstPlayer: PlayerForElo<E>,
    secondPlayer: PlayerForElo<E>,
    result: Outcomes
) {
    private val result: Outcomes
    private val first: PlayerForElo<E>
    private val second: PlayerForElo<E>

    enum class Outcomes {
        ePlayer_One_Won, ePlayer_Two_Won, eDraw
    }

    /**
     * The opponent of the given player in this match.
     * @param player played in this match.
     */
    fun getOpponent(player: PlayerForElo<E>): PlayerForElo<E> {
        return when {
            player === first -> {
                second
            }
            player === second -> {
                first
            }
            else -> {
                throw IllegalArgumentException(
                    "Given player was not in this match."
                )
            }
        }
    }

    /**
     * The score of the given player in this match.
     * @return 1 for a win, 0 for a loss, and 0.5 for a tie.
     * @throws IllegalArgumentException if player was not in this match.
     */
    fun getScore(player: PlayerForElo<E>): Double {
        var score = 0.5
        score = if (player === first) {
            when (result) {
                Outcomes.ePlayer_One_Won -> 1.0
                Outcomes.ePlayer_Two_Won -> 0.0
                Outcomes.eDraw -> .5
            }
        } else if (player === second) {
            when (result) {
                Outcomes.ePlayer_One_Won -> 0.0
                Outcomes.ePlayer_Two_Won -> 1.0
                Outcomes.eDraw -> .5
            }
        } else {
            throw IllegalArgumentException(
                "Given player was not in this match."
            )
        }
        return score
    }

    init {
        first = firstPlayer
        second = secondPlayer
        this.result = result
        firstPlayer.addMatchForElo(this)
        secondPlayer.addMatchForElo(this)
    }
}
