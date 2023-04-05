package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinTechnician extends TestGame {

    @Test
    public void testDamageRandomOpponentOnDeath() {
        int card = create("creatures/goblin_technician", player, Components.CREATURE_ZONE);
        destroy(card);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 2);
    }
}
