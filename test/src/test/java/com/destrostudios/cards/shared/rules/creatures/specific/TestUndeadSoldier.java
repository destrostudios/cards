package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestUndeadSoldier extends TestGame {

    @Test
    public void testSummonZombieOnDeath() {
        int card = create("creatures/undead_soldier", player, Components.CREATURE_ZONE);
        destroy(card);
        assertOneCard(player, Components.CREATURE_ZONE, "Zombie");
    }
}
