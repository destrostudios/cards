package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestConfusedSoldier extends TestGame {

    @Test
    public void testDamageOnSummon() {
        int card = create("creatures/confused_soldier", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
        assertHealthAndDamaged(opponent, GameConstants.PLAYER_HEALTH - 1);
    }
}
