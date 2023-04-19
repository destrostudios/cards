package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestWalkingMortuary extends TestGame {

    @Test
    public void testSummonZombiesOnDeath() {
        int card = create("creatures/walking_mortuary", player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Zombie", 9);
    }
}
