package com.destrostudios.cards.shared.rules.mechanics;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import com.destrostudios.cards.shared.rules.buffs.AddBuffEvent;
import org.junit.jupiter.api.Test;

public class TestRemoveBuffs extends TestGame {

    @Test
    public void testRemoveCardBuffsOnRemoveFromBoard() {
        int creature = createCreature(player, Components.CREATURE_ZONE);
        int buff = data.createEntity();
        fire(new AddBuffEvent(creature, buff));
        destroy(creature);
        assertHasNoComponent(creature, Components.BUFFS);
    }

    @Test
    public void testRemoveDefaultCastFromHandSpellBuffsOnRemoveFromHand() {
        int card = createCard(player, Components.HAND);
        int defaultCastFromHandSpell = getDefaultCastFromHandSpell(card);
        int buff = data.createEntity();
        fire(new AddBuffEvent(defaultCastFromHandSpell, buff));
        castFromHand(card);
        assertHasNoComponent(defaultCastFromHandSpell, Components.BUFFS);
    }
}
