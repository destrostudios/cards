package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestConjuredLightning extends TestGame {

    @Test
    public void testDamageAndHealCreaturesOnCast() {
        int[] allyCreatures = createVanillas(2, 0, 0, 5, player, Components.Zone.CREATURE_ZONE);
        damage(allyCreatures, 4);
        int[] opponentCreatures = createVanillas(2, 0, 0, 4, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("spells/conjured_lightning", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(allyCreatures, 4);
        assertHealthAndDamaged(opponentCreatures, 1);
    }
}
