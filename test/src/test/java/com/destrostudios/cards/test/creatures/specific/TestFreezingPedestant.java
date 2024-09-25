package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFreezingPedestant extends TestGame {

    @Test
    public void testSummonFreezingGhostAtEndOfTurnOnDeath() {
        int card = create("creatures/freezing_pedestant", player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        endTurn(player);
        assertOneCard(player, Components.Zone.CREATURE_ZONE, "Freezing Ghost");
    }
}
