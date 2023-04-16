package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestAwakenedSuccubus extends TestGame {

    @Test
    public void testDiscardOnSummon() {
        int handCard = createCard(player, Components.Zone.HAND);
        int card = create("creatures/awakened_succubus", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(handCard, Components.Zone.GRAVEYARD);
    }
}
