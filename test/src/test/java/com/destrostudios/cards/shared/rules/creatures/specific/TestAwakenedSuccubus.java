package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestAwakenedSuccubus extends TestGame {

    @Test
    public void testDiscardOnSummon() {
        int handCard = createCard(player, Components.HAND);
        int card = create("creatures/awakened_succubus", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(handCard, Components.GRAVEYARD);
    }
}
