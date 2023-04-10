package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestAdventure extends TestGame {

    @Test
    public void testDrawCreatureOnCast() {
        int creature = createVanilla(5, 0, 1, player, Components.LIBRARY);
        int card = create("spells/adventure", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(creature, Components.HAND);
    }
}
