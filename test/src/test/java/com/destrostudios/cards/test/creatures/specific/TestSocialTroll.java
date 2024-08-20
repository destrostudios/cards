package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSocialTroll extends TestGame {

    @Test
    public void testReduceManaCostForEachCreatureOnBoard() {
        int card = create("creatures/social_troll", player, Components.Zone.HAND);
        assertManaCost(card, 7);
        for (int expectedManaCost : new int[] { 6, 5, 4, 3, 2, 1, 0, 0 }) {
            createCard(player, Components.Zone.CREATURE_ZONE);
            assertManaCost(card, expectedManaCost);
        }
    }
}
