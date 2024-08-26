package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldSparks extends TestGame {

    @Test
    public void testDamageRandomOpponentsOnCast() {
        int card = create("spells/underworld_sparks", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 2);
    }

    @Test
    public void testDamageRandomOpponentsOnDiscard() {
        int card = create("spells/underworld_sparks", player, Components.Zone.HAND);
        discard(card);
        assertHealth(opponent, GameConstants.PLAYER_HEALTH - 4);
    }
}
