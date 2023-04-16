package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestWoodedChimera extends TestGame {

    @Test
    public void testSummonChildrenOnSummon() {
        int card = create("creatures/wooded_chimera", player, Components.Zone.HAND);
        castFromHand(card);
        assertOneCard(player, Components.Zone.CREATURE_ZONE, "Wooded Chimera's Firstborn");
        assertOneCard(player, Components.Zone.CREATURE_ZONE, "Wooded Chimera's Secondborn");
        assertOneCard(player, Components.Zone.CREATURE_ZONE, "Wooded Chimera's Thirdborn");
    }
}
