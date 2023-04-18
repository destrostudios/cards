package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSunray extends TestGame {

    @Test
    public void testHealCreaturesOnCast() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 0, 6, Components.Zone.CREATURE_ZONE);
        damage(creatures, 5);
        int card = create("spells/sunray", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 5);
    }
}
