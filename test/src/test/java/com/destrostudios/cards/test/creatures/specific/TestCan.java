package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestCan extends TestGame {

    @Test
    public void testDiscardAndDrawCardSpell() {
        int libraryCard = createCard(player, Components.Zone.LIBRARY);
        int card = create("creatures/can", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, 1, null);
        cast(spell);
        assertHasComponent(card, Components.Zone.GRAVEYARD);
        assertHasComponent(libraryCard, Components.Zone.HAND);
    }
}
