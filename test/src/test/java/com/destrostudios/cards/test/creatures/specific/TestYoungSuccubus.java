package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestYoungSuccubus extends TestGame {

    @Test
    public void testDamageOnSummon() {
        int card = create("creatures/young_succubus", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 5);
    }
}
