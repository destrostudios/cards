package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestDarkRider extends TestGame {

    @Test
    public void testDamageOpponentOnDeath() {
        int card = create("creatures/dark_rider", player, Components.CREATURE_ZONE);
        destroy(card);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 2);
    }
}
