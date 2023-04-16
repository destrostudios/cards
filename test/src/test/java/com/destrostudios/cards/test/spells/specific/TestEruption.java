package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestEruption extends TestGame {

    @Test
    public void testSummonPebblesOnCast() {
        int card = create("spells/eruption", player, Components.Zone.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Pebble", 10);
    }
}
