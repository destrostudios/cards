package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBlueLagoon extends TestGame {

    @Test
    public void testDrawBeastsOnCast() {
        int[] beasts = createCreatures(3, player, Components.Zone.LIBRARY);
        forEach(beasts, beast -> data.setComponent(beast, Components.Tribe.BEAST));
        int card = create("spells/blue_lagoon", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(beasts, Components.Zone.HAND);
    }
}
