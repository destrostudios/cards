package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUltriiaFlowerOfCorruption extends TestGame {

    @Test
    public void testReduceManaCostOnPlayerDamage() {
        int card = create("creatures/ultriia_flower_of_corruption", player, Components.Zone.HAND);
        damage(opponent, 1);
        assertManaCost(card, 7);
    }
}
