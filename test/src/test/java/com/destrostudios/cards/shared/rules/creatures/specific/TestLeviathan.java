package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestLeviathan extends TestGame {

    @Test
    public void testDamageCreaturesOnSummon() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 4, 4, Components.CREATURE_ZONE);
        int card = create("creatures/leviathan", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 1);
    }
}
