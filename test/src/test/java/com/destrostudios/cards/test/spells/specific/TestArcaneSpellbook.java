package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestArcaneSpellbook extends TestGame {

    @Test
    public void testDiscoverSpellAndReduceItsManaCost() {
        int card = create("spells/arcane_spellbook", player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        int spell = getAndAssertFirstCard(player, Components.Zone.HAND, getDiscoveredCardName(DiscoverPool.SPELL));
        assertManaCost(spell, 2);
    }
}
