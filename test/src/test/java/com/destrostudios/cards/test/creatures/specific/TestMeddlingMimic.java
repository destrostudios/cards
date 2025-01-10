package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestMeddlingMimic extends TestGame {

    @Test
    public void testDiscoverCreatureAndBuffItself() {
        int card = create("creatures/meddling_mimic", player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        getAndAssertFirstCard(player, Components.Zone.HAND, getDiscoveredCardName(DiscoverPool.CREATURE));
        assertAttack(card, 3);
        assertHealth(card, 3);
    }
}
