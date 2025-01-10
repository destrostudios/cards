package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFrozenExcavation extends TestGame {

    @Test
    public void testDiscoverCreatureAndSummonIt() {
        int card = create("spells/frozen_excavation", player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        getAndAssertFirstCard(player, Components.Zone.CREATURE_ZONE, getDiscoveredCardName(DiscoverPool.CREATURE_THAT_COSTS_8_OR_MORE));
    }
}
