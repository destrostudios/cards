package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestActualReallyBigGoblin extends TestGame {

    @Test
    public void testSummonGoblinsOnDeath() {
        int card = create("creatures/actual_really_big_goblin", player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Goblin", 3);
    }
}
