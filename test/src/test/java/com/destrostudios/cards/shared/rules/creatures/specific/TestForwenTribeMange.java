package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestForwenTribeMange extends TestGame {

    @Test
    public void testAddThunderToHandOnDeath() {
        int card = create("creatures/forwen_tribe_mage", player, Components.CREATURE_ZONE);
        destroy(card);
        assertOneCard(player, Components.HAND, "Thunder");
    }
}
