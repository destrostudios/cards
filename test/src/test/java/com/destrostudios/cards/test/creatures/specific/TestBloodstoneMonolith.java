package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBloodstoneMonolith extends TestGame {

    @Test
    public void testDamageOnSummonAndHealOnAttack() {
        int card = create("creatures/bloodstone_monolith", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 8);
        attack(card, opponent);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 4);
    }
}
