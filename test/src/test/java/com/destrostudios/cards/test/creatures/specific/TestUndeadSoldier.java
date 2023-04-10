package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUndeadSoldier extends TestGame {

    @Test
    public void testSummonZombieOnDeath() {
        int card = create("creatures/undead_soldier", player, Components.CREATURE_ZONE);
        destroy(card);
        assertOneCard(player, Components.CREATURE_ZONE, "Zombie");
    }
}
