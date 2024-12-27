package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestReleaseTheBeasts extends TestGame {

    @Test
    public void testSummonReleasedBeastsOnCast() {
        int card = create("spells/release_the_beasts", player, Components.Zone.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Released Beast", 2);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 8);
    }
}
