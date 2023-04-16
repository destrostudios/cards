package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestTechnology extends TestGame {

    @Test
    public void testDrawOnCast() {
        int[] libraryCards = createCards(2, player, Components.Zone.LIBRARY);
        int card = create("spells/technology", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(libraryCards, Components.Zone.HAND);
    }
}
