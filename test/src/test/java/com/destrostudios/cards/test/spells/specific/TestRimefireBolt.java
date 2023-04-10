package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestRimefireBolt extends TestGame {

    @Test
    public void testDamageCreatureOnCast() {
        int creature = createVanilla(0, 0, 5, opponent, Components.CREATURE_ZONE);
        int card = create("spells/rimefire_bolt", player, Components.HAND);
        castFromHand(card, creature);
        assertHealthAndDamaged(creature, 1);
    }
}
