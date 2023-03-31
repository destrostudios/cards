package com.destrostudios.cards.shared.rules.creatures.custom;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestDarkRider extends TestGame {

    @Test
    public void testDamageOnDeath() {
        int card = create("creatures/dark_rider", player, Components.CREATURE_ZONE);
        destroy(card);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 2);
    }
}
