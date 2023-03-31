package com.destrostudios.cards.shared.rules.creatures.custom;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestElvenQueen extends TestGame {

    @Test
    public void testSummonElvenGuardsOnSummon() {
        int card = create("creatures/elven_queen", player, Components.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.CREATURE_ZONE, "Elven Guard", 2);
    }
}
