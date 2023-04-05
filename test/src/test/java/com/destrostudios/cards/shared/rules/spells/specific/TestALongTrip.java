package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestALongTrip extends TestGame {

    @Test
    public void testDrawGoblinsOnCast() {
        int[] goblins = createVanillas(3, 0, 0, 1, player, Components.LIBRARY);
        forEach(goblins, goblin -> data.setComponent(goblin, Components.Tribe.GOBLIN));
        int card = create("spells/a_long_trip", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(goblins, Components.HAND);
    }
}
