package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestElvenQueen extends TestGame {

    @Test
    public void testSummonElvenGuardsOnSummon() {
        int card = create("creatures/elven_queen", player, Components.Zone.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Elven Guard", 2);
    }
}
