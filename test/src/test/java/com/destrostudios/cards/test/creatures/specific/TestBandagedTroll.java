package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBandagedTroll extends TestGame {

    @Test
    public void testReduceManaCostForEachDamageDuringOwnTurn() {
        int card = create("creatures/bandaged_troll", player, Components.Zone.HAND);
        assertManaCost(card, 10);
        for (int expectedManaCost : new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0 }) {
            damage(player, 1);
            assertManaCost(card, expectedManaCost);
        }
    }
}
