package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestSunray extends TestGame {

    @Test
    public void testHealCreaturesOnCast() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 0, 5, Components.CREATURE_ZONE);
        damage(creatures, 4);
        int card = create("spells/sunray", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 4);
    }
}
