package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestForwenTribeMange extends TestGame {

    @Test
    public void testAddThunderToHandOnDeath() {
        int card = create("creatures/forwen_tribe_mage", player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        assertOneCard(player, Components.Zone.HAND, "Thunder");
    }
}
