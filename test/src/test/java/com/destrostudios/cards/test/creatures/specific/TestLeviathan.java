package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestLeviathan extends TestGame {

    @Test
    public void testDamageCreaturesOnSummon() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 0, 3, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/leviathan", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 1);
    }
}
