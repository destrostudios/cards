package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestWanderingWizard extends TestGame {

    @Test
    public void testDrawSpellOnSummon() {
        int spell = createSpell(player, Components.Zone.LIBRARY);
        int card = create("creatures/wandering_wizard", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(spell, Components.Zone.HAND);
    }
}
