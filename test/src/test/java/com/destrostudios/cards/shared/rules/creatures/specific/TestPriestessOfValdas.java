package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestPriestessOfValdas extends TestGame {

    @Test
    public void testDrawOnHeal() {
        int libraryCard = createCard(player, Components.LIBRARY);
        int card = create("creatures/priestess_of_valdas", player, Components.CREATURE_ZONE);
        damage(card, 1);
        heal(card, 1);
        assertHasComponent(libraryCard, Components.HAND);
    }
}
