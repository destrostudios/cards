package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestWizardTroll extends TestGame {

    @Test
    public void testReduceManaCostForEachSpellPlayed() {
        int card = create("creatures/wizard_troll", player, Components.Zone.HAND);
        assertManaCost(card, 10);
        for (int expectedManaCost : new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0 }) {
            int spell = createSpell(player, Components.Zone.HAND);
            castFromHand(spell);
            assertManaCost(card, expectedManaCost);
        }
    }
}
