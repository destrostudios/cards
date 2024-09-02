package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestOven extends TestGame {

    @Test
    public void testDamageRandomOpponentOnTurnEnd() {
        create("creatures/oven", player, Components.Zone.CREATURE_ZONE);
        endTurn(player);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 1);
    }
}
