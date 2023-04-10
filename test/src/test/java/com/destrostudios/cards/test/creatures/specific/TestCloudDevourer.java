package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestCloudDevourer extends TestGame {

    @Test
    public void testHealOnTurnEnd() {
        int card = create("creatures/cloud_devourer", player, Components.CREATURE_ZONE);
        damage(card, 3);
        endTurn(player);
        assertHealthAndDamaged(card, 5);
    }
}
