package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestTerrorFromAbove extends TestGame {

    @Test
    public void testDestroyCreaturesOnSummon() {
        int[] creatures = createCreaturesForBothPlayers(2, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/terror_from_above", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(creatures, Components.Zone.GRAVEYARD);
    }
}
