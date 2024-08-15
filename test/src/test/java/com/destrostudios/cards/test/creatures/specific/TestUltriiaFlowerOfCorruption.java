package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUltriiaFlowerOfCorruption extends TestGame {

    @Test
    public void testReduceManaCostOnPlayerDamage() {
        int card = create("creatures/ultriia_flower_of_corruption", player, Components.Zone.HAND);
        assertManaCost(card, 8);
        for (int expectedManaCost : new int[] { 7, 6, 5, 4, 3, 2, 1, 0, 0 }) {
            damage(opponent, 1);
            assertManaCost(card, expectedManaCost);
        }
    }
}
