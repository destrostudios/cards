package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHoarderTroll extends TestGame {

    @Test
    public void testReduceManaCostForEachCardInHand() {
        int card = create("creatures/hoarder_troll", player, Components.Zone.HAND);
        assertManaCost(card, 10);
        for (int expectedManaCost : new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0 }) {
            createCard(player, Components.Zone.HAND);
            assertManaCost(card, expectedManaCost);
        }
    }
}
