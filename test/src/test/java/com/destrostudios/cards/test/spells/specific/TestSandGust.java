package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSandGust extends TestGame {

    @Test
    public void testReturnCreatureToHand() {
        int creature = createCreature(opponent, Components.Zone.CREATURE_ZONE);
        int card = create("spells/sand_gust", player, Components.Zone.HAND);
        castFromHand(card, creature);
        assertHasComponent(creature, Components.Zone.HAND);
    }
}
