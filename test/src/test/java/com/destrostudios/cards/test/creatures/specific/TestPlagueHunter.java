package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestPlagueHunter extends TestGame {

    @Test
    public void testDrawCardOnSummonWhenEmptyHand() {
        int libraryCard = createCard(player, Components.Zone.LIBRARY);
        int card = create("creatures/plague_hunter", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(libraryCard, Components.Zone.HAND);
    }

    @Test
    public void testDrawNoCardOnSummonWhenNotEmptyHand() {
        int libraryCard = createCard(player, Components.Zone.LIBRARY);
        createCard(player, Components.Zone.HAND);
        int card = create("creatures/plague_hunter", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(libraryCard, Components.Zone.LIBRARY);
    }
}
