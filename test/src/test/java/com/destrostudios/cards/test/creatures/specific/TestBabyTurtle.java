package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBabyTurtle extends TestGame {

    @Test
    public void testSummonTarasqueOnDeath() {
        int card = create("creatures/baby_turtle", player, Components.CREATURE_ZONE);
        destroy(card);
        assertOneCard(player, Components.CREATURE_ZONE, "Tarasque");
    }
}
