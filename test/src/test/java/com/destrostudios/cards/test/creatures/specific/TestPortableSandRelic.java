package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestPortableSandRelic extends TestGame {

    @Test
    public void testAddSandstormToHandOnDeath() {
        int card = create("creatures/portable_sand_relic", player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        assertOneCard(player, Components.Zone.HAND, "Sandstorm");
    }
}
