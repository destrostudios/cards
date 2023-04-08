package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestUltriiaTheFlowerOfCorruption extends TestGame {

    @Test
    public void testReduceManaCostOnPlayerDamage() {
        int card = create("creatures/ultriia_the_flower_of_corruption", player, Components.HAND);
        damage(opponent, 1);
        assertManaCost(card, 7);
    }
}
