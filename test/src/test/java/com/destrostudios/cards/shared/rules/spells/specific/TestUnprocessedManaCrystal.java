package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnprocessedManaCrystal extends TestGame {

    @Test
    public void testReduceManaCostOnSummon() {
        int spell = createSpell(2, player, Components.HAND);
        int card = create("spells/unprocessed_mana_crystal", player, Components.HAND);
        castFromHand(card, spell);
        assertManaCost(spell, 1);
    }
}
