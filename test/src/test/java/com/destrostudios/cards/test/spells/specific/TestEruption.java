package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestEruption extends TestGame {

    @Test
    public void testSummonPebblesOnCast() {
        int card = create("spells/eruption", player, Components.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.CREATURE_ZONE, "Pebble", 10);
    }
}
