package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestConjuredLightning extends TestGame {

    @Test
    public void testDamageAndHealCreaturesOnCast() {
        int[] allyCreatures = createVanillas(2, 0, 0, 4, player, Components.CREATURE_ZONE);
        damage(allyCreatures, 3);
        int[] opponentCreatures = createVanillas(2, 0, 0, 3, opponent, Components.CREATURE_ZONE);
        int card = create("spells/conjured_lightning", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(allyCreatures, 3);
        assertHealthAndDamaged(opponentCreatures, 1);
    }
}
