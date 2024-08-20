package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestLibraryTroll extends TestGame {

    @Test
    public void testReduceManaCostForEachCardDrawn() {
        int card = create("creatures/library_troll", player, Components.Zone.HAND);
        assertManaCost(card, 20);
        for (int expectedManaCost : new int[] { 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0 }) {
            createCard(player, Components.Zone.LIBRARY);
            draw(player);
            assertManaCost(card, expectedManaCost);
        }
    }
}
