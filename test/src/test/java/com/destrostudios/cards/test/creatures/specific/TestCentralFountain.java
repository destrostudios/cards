package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestCentralFountain extends TestGame {

    @Test
    public void testHealOnTurnEnd() {
        damage(player, 4);
        create("creatures/central_fountain", player, Components.Zone.CREATURE_ZONE);
        endTurn(player);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
    }
}
