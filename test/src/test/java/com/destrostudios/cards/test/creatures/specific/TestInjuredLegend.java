package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestInjuredLegend extends TestGame {

    @Test
    public void testTakeDamageOnSummon() {
        int card = create("creatures/injured_legend", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(card, 3);
    }
}
