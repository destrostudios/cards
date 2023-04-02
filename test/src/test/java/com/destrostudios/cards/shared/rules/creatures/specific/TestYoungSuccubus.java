package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestYoungSuccubus extends TestGame {

    @Test
    public void testDamageOnSummon() {
        int card = create("creatures/young_succubus", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 5);
    }
}
