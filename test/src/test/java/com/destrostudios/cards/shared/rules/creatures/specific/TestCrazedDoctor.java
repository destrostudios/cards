package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestCrazedDoctor extends TestGame {

    @Test
    public void testHealOnSummon() {
        data.setComponent(player, Components.Stats.DAMAGED, 15);
        int card = create("creatures/crazed_doctor", player, Components.HAND);
        castFromHand(card);
        assertHealth(player, GameConstants.PLAYER_HEALTH - 5);
    }
}
