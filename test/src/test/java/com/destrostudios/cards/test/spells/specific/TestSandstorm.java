package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSandstorm extends TestGame {

    @Test
    public void testPutAllCreaturesToHandsOnCast() {
        int[] creatures = createCreaturesForBothPlayers(2, Components.Zone.CREATURE_ZONE);
        int card = create("spells/sandstorm", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(creatures, Components.Zone.HAND);
    }
}
