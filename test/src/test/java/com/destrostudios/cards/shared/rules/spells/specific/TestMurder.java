package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestMurder extends TestGame {

    @Test
    public void testDestroyCreatureOnCast() {
        int creature = createCreature(opponent, Components.CREATURE_ZONE);
        int card = create("spells/murder", player, Components.HAND);
        castFromHand(card, creature);
        assertHasComponent(creature, Components.GRAVEYARD);
    }
}
