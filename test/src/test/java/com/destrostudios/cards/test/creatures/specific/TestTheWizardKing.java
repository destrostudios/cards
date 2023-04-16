package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestTheWizardKing extends TestGame {

    @Test
    public void testSetSpellManaCostToZeroOnSummon() {
        int spell = createSpell(999, player, Components.Zone.HAND);
        int card = create("creatures/the_wizard_king", player, Components.Zone.HAND);
        castFromHand(card, spell);
        assertManaCost(spell, 0);
    }
}
