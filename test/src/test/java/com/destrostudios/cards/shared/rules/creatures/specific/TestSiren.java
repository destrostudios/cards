package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestSiren extends TestGame {

    @Test
    public void testReduceSpellsManaCostWhenOnBoard() {
        int spell = createSpell(2, player, Components.HAND);
        create("creatures/siren", player, Components.CREATURE_ZONE);
        assertManaCost(spell, 1);
    }
}
