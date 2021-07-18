package com.example.academey.domain.match

import java.util.ArrayList

/**
 * Represents a player with matches and scores.
 *
 */
class PlayerForElo<E>(
    /**
     * Get our ID.
     */
    val iD: E,
    var rating: Double = 0.0
) {
    private val matches: MutableList<MatchForElo<E>>

    /**
     * Record us playing in a match. Change our score to reflect playing
     * an opponent.
     * @see [Source](https://en.wikipedia.org/wiki/Elo_rating_system.Mathematical_details)
     */
    fun addMatchForElo(match: MatchForElo<E>) {
        matches.add(match)
        rating += k * (match.getScore(this) - getWinOdds(match.getOpponent(this)))
    }

    /**
     * Get the odds of us winning against the given opponent.
     * @see [Source](https://en.wikipedia.org/wiki/Elo_rating_system.Mathematical_details)
     */
    fun getWinOdds(opponent: PlayerForElo<E>): Double {
        return 1.0 / (
            1 + Math.pow(
                10.0,
                (opponent.rating - rating) / 400.0
            )
            )
    }

    /**
     * Get the weight we should assign to new matches.
     */
    private val k: Double
        get() {
            return 16.0
            // var k = 25.0
            // if (matches.size > 15) {
            //     k = 16.0
            // }
            // return k
        }

    /**
     * Create a player with the given ID.
     */
    init {
        matches = ArrayList<MatchForElo<E>>()
    }
}
