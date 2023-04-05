package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestHobgoblinCannoneer extends TestGame {

    @Test
    public void testDamageRandomOpponentsOnAttack() {
        int card = create("creatures/hobgoblin_cannoneer", player, Components.CREATURE_ZONE);
        attack(card, opponent);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 6);
    }
}
