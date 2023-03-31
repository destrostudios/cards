package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestInjuredLegend extends TestGame {

    @Test
    public void testTakeDamageOnSummon() {
        int card = create("creatures/injured_legend", player, Components.CREATURE_ZONE);
        castFromHand(card);
        assertHealthAndDamaged(card, 2);
    }
}
