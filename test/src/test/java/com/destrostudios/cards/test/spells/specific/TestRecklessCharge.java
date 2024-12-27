package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestRecklessCharge extends TestGame {

    @Test
    public void testDamageOnCast() {
        int[] targets = createVanillas(2, 0, 0, 3, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("spells/reckless_charge", player, Components.Zone.HAND);
        castFromHand(card, targets);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 2);
        assertHealthAndDamaged(targets, 1);
    }
}
