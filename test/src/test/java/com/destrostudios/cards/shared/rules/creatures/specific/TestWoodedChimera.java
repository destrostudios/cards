package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestWoodedChimera extends TestGame {

    @Test
    public void testSummonChildrenOnSummon() {
        int card = create("creatures/wooded_chimera", player, Components.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.CREATURE_ZONE, "Wooded Chimera's Firstborn", 1);
        assertCardsCount(player, Components.CREATURE_ZONE, "Wooded Chimera's Secondborn", 1);
        assertCardsCount(player, Components.CREATURE_ZONE, "Wooded Chimera's Thirdborn", 1);
    }
}
