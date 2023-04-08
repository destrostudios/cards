package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestAstralLich extends TestGame {

    @Test
    public void testDamageRandomOpponentOnHeal() {
        int card = create("creatures/astral_lich", player, Components.CREATURE_ZONE);
        damage(card, 1);
        heal(card, 1);
        assertHealthAndDamaged(opponent, GameConstants.PLAYER_HEALTH - 2);
    }
}
