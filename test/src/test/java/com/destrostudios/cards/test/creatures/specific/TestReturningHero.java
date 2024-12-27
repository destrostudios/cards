package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestReturningHero extends TestGame {

    @Test
    public void testBuffOnSummonWhenOwnerLowHealth() {
        damage(player, GameConstants.PLAYER_HEALTH - 20);
        int card = create("creatures/returning_hero", player, Components.Zone.HAND);
        castFromHand(card);
        assertAttack(card, 4);
        assertHealth(card, 5);
    }
}
