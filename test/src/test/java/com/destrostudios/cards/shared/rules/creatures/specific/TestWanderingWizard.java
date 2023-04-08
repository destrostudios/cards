package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestWanderingWizard extends TestGame {

    @Test
    public void testDrawSpellOnSummon() {
        int spell = createSpell(player, Components.LIBRARY);
        int card = create("creatures/wandering_wizard", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(spell, Components.HAND);
    }
}
