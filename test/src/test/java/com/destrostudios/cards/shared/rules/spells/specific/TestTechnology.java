package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestTechnology extends TestGame {

    @Test
    public void testDrawOnCast() {
        int[] libraryCards = createCards(2, player, Components.LIBRARY);
        int card = create("spells/technology", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(libraryCards, Components.HAND);
    }
}
