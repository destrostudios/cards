package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestPlagueHunter extends TestGame {

    @Test
    public void testDrawOnSummonWhenEmptyHand() {
        int libraryCard = createCard(player, Components.Zone.LIBRARY);
        int card = create("creatures/plague_hunter", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(libraryCard, Components.Zone.HAND);
    }
}
