package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestPlagueHunter extends TestGame {

    @Test
    public void testDrawOnSummonWhenEmptyHand() {
        int libraryCard = createCard(player, Components.LIBRARY);
        int card = create("creatures/plague_hunter", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(libraryCard, Components.HAND);
    }
}
