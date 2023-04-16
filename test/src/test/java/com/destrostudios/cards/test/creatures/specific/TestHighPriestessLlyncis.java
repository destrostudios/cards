package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHighPriestessLlyncis extends TestGame {

    @Test
    public void testHealCharacterOnSummon() {
        damage(player, 5);
        int card = create("creatures/high_priestess_llyncis", player, Components.Zone.HAND);
        castFromHand(card, player);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
    }

    @Test
    public void testDamageCharacterOnSummon() {
        int card = create("creatures/high_priestess_llyncis", player, Components.Zone.HAND);
        castFromHand(card, opponent);
        assertHealthAndDamaged(opponent, GameConstants.PLAYER_HEALTH - 7);
    }
}
