package com.example.academey.domain.match

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class PlayerForEloTest {
    /**
     * Test that we can make a player.
     */
    @Test
    fun testPlayer() {
        val rick: PlayerForElo<String> = PlayerForElo("Hello")
    }

    /**
     * Test that we can change a player's rating.
     */
    @Test
    fun testSetRating() {
        val rick: PlayerForElo<String> = PlayerForElo("Hello")
        assertTrue(rick.rating.equals(1000.0))
        rick.rating = 2000.0
        assertTrue(rick.rating.equals(2000.0))
    }

    /**
     * Test that we calculate win odds correctly.
     */
    @Test
    fun testWinOdds() {
        val rick: PlayerForElo<String> = PlayerForElo("Rick")
        val john: PlayerForElo<String> = PlayerForElo("John")
        assertEquals(rick.getWinOdds(john), .5, .01)
        assertEquals(john.getWinOdds(rick), .5, .01)
        rick.rating = 1500.0
        assertEquals(rick.getWinOdds(john), 1 - .053, .01)
        assertEquals(john.getWinOdds(rick), .053, .01)
    }

    /**
     * Test that we change our scores correctly.
     */
    @Test
    fun testAdjustScore() {
        val rick: PlayerForElo<String> = PlayerForElo("Rick")
        val john: PlayerForElo<String> = PlayerForElo("John")
        var drawMatch: MatchForElo<String> = MatchForElo(rick, john, MatchForElo.Outcomes.eDraw)
        rick.addMatchForElo(drawMatch)
        john.addMatchForElo(drawMatch)
        var rickWinMatch: MatchForElo<String> = MatchForElo(rick, john, MatchForElo.Outcomes.ePlayer_One_Won)
        rick.addMatchForElo(rickWinMatch)
        john.addMatchForElo(rickWinMatch)
        assertEquals(rick.rating, 1015.63, .01)
        assertEquals(john.rating, 984.72, .01)
    }
}
