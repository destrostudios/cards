package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinTourguide extends TestGame {

    @Test
    public void testDiscoverGoblinAndSummonIt() {
        int allyGoblin = createCreature(player, Components.Zone.CREATURE_ZONE);
        data.setComponent(allyGoblin, Components.Tribe.GOBLIN);
        int card = create("creatures/goblin_tourguide", player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        getAndAssertCard(player, Components.Zone.CREATURE_ZONE, 2, getDiscoveredCardName(DiscoverPool.GOBLIN_THAT_COSTS_2_OR_LESS));
    }

    @Test
    public void testDiscoverGoblinAndDontSummonIt() {
        int card = create("creatures/goblin_tourguide", player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        getAndAssertFirstCard(player, Components.Zone.HAND, getDiscoveredCardName(DiscoverPool.GOBLIN_THAT_COSTS_2_OR_LESS));
    }
}
