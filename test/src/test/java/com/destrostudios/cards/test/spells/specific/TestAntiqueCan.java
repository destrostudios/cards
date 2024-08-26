package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestAntiqueCan extends TestGame {

    @Test
    public void testDiscardTargetAndDrawOnCast() {
        int[] libraryCards = createCards(2, player, Components.Zone.LIBRARY);
        int target = createCard(player, Components.Zone.HAND);
        int card = create("spells/antique_can", player, Components.Zone.HAND);
        castFromHand(card, target);
        assertHasComponent(target, Components.Zone.GRAVEYARD);
        assertHasComponent(libraryCards, Components.Zone.HAND);
    }
}
