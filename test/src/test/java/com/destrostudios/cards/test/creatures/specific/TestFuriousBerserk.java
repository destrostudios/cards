package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFuriousBerserk extends TestGame {

    @Test
    public void testBuffOnDamageTaken() {
        int card = create("creatures/furious_berserk", player, Components.Zone.CREATURE_ZONE);
        damage(card, 1);
        assertAttack(card, 2);
    }
}
