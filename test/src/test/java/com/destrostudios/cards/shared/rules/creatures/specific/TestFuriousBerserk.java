package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestFuriousBerserk extends TestGame {

    @Test
    public void testBuffOnDamageTaken() {
        int card = create("creatures/furious_berserk", player, Components.CREATURE_ZONE);
        damage(card, 1);
        assertAttack(card, 2);
    }
}
