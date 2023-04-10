package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestTerrorFromAbove extends TestGame {

    @Test
    public void testDestroyCreaturesOnSummon() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 1, 1, Components.CREATURE_ZONE);
        int card = create("creatures/terror_from_above", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(creatures, Components.GRAVEYARD);
    }
}
