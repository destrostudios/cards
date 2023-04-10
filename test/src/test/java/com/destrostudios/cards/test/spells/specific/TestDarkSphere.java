package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDarkSphere extends TestGame {

    @Test
    public void testDamageOnCast() {
        int card = create("spells/dark_sphere", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(opponent, GameConstants.PLAYER_HEALTH - 4);
    }
}
