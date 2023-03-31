package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestThunder extends TestGame {

    @Test
    public void testDamageOnCast() {
        int creature = createVanilla(0, 0, 4, opponent, Components.CREATURE_ZONE);
        int card = create("spells/thunder", player, Components.HAND);
        castFromHand(card, creature);
        assertHealthAndDamaged(creature, 1);
    }
}
