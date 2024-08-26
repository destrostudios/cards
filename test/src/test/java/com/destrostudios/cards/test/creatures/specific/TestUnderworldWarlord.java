package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldWarlord extends TestGame {

    @Test
    public void testDiscardAndDrawSpell() {
        int[] libraryCards = createCards(2, player, Components.Zone.LIBRARY);
        int[] targets = createCards(2, player, Components.Zone.HAND);
        int card = create("creatures/underworld_warlord", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, 1, 1);
        cast(spell, targets);
        assertHasComponent(targets, Components.Zone.GRAVEYARD);
        assertHasComponent(libraryCards, Components.Zone.HAND);
    }
}
