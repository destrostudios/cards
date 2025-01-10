package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestVersatileChef extends TestGame {

    @Test
    public void testDiscoverCreatureAndHealPlayer() {
        damage(player, 6);
        int card = create("creatures/versatile_chef", player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        getAndAssertFirstCard(player, Components.Zone.HAND, getDiscoveredCardName(DiscoverPool.CREATURE));
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
    }
}
