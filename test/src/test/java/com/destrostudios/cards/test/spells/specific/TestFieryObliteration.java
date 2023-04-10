package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFieryObliteration extends TestGame {

    @Test
    public void testDamageOpponentCreaturesOnCast() {
        int[] creatures = createVanillas(2, 0, 0, 5, opponent, Components.CREATURE_ZONE);
        int card = create("spells/fiery_obliteration", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 1);
    }
}
