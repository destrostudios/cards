package com.destrostudios.cards.test.mechanics;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import com.destrostudios.cards.shared.rules.buffs.AddBuffEvent;
import org.junit.jupiter.api.Test;

public class TestRemoveBuffs extends TestGame {

    @Test
    public void testRemoveCardBuffsOnRemoveFromCreatureZone() {
        int creature = createCreature(player, Components.Zone.CREATURE_ZONE);
        int buff = data.createEntity();
        fire(new AddBuffEvent(creature, buff));
        destroy(creature);
        assertHasNoComponent(creature, Components.BUFFS);
    }

    @Test
    public void testRemoveDefaultCastFromHandSpellBuffsOnRemoveFromHand() {
        int card = createCard(player, Components.Zone.HAND);
        int defaultCastFromHandSpell = getDefaultCastFromHandSpell(card);
        int buff = data.createEntity();
        fire(new AddBuffEvent(defaultCastFromHandSpell, buff));
        castFromHand(card);
        assertHasNoComponent(defaultCastFromHandSpell, Components.BUFFS);
    }
}
