package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSiren extends TestGame {

    @Test
    public void testReduceSpellsManaCostWhenOnBoard() {
        int spell = createSpell(2, player, Components.Zone.HAND);
        create("creatures/siren", player, Components.Zone.CREATURE_ZONE);
        assertManaCost(spell, 1);
    }
}
