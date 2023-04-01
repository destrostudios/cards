package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestNightmareDemon extends TestGame {

    @Test
    public void testDamageOpponentOnDamageTaken() {
        int card = create("creatures/nightmare_demon", player, Components.CREATURE_ZONE);
        damage(card, 1);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 2);
    }
}
