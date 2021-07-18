package com.example.academey.domain.match

import org.junit.Test

internal class MatchForEloTest {
    /**
     * Test that we can make a match.
     */
    @Test
    fun testMatch() {
        val rick: PlayerForElo<String> = PlayerForElo("Hello")
        val john: PlayerForElo<String> = PlayerForElo("Hello")
        var gae: MatchForElo<String> = MatchForElo(rick, john, MatchForElo.Outcomes.eDraw)
        gae = MatchForElo(rick, john, MatchForElo.Outcomes.ePlayer_One_Won)
    }
}
