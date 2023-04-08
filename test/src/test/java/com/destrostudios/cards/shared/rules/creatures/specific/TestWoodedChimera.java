package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestWoodedChimera extends TestGame {

    @Test
    public void testSummonChildrenOnSummon() {
        int card = create("creatures/wooded_chimera", player, Components.HAND);
        castFromHand(card);
        assertOneCard(player, Components.CREATURE_ZONE, "Wooded Chimera's Firstborn");
        assertOneCard(player, Components.CREATURE_ZONE, "Wooded Chimera's Secondborn");
        assertOneCard(player, Components.CREATURE_ZONE, "Wooded Chimera's Thirdborn");
    }
}
