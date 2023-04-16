package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBonfire extends TestGame {

    @Test
    public void testHealOnCast() {
        damage(player, 10);
        int card = create("spells/bonfire", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 4);
    }
}
