package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestCopperfangTribesman extends TestGame {

    @Test
    public void testBuffBeastsOnSummon() {
        int[] beasts = createVanillas(2, 0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        forEach(beasts, beast -> data.setComponent(beast, Components.Tribe.BEAST));
        int card = create("creatures/copperfang_tribesman", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealth(beasts, 3);
    }
}
