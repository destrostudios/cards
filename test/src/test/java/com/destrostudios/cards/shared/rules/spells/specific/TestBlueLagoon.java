package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestBlueLagoon extends TestGame {

    @Test
    public void testDrawBeastsOnCast() {
        int[] beasts = createCreatures(3, player, Components.LIBRARY);
        forEach(beasts, beast -> data.setComponent(beast, Components.Tribe.BEAST));
        int card = create("spells/blue_lagoon", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(beasts, Components.HAND);
    }
}
