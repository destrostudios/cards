package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestDemonSamurai extends TestGame {

    @Test
    public void testDamageOpponentsOnAttack() {
        int card = create("creatures/demon_samurai", player, Components.CREATURE_ZONE);
        attack(card, opponent);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 4);
    }
}
