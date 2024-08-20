package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFireSpirit extends TestGame {

    @Test
    public void testDiscardRandomOpponentCardOnSummon() {
        int allyCreature = createCreature(player, Components.Zone.CREATURE_ZONE);
        int opponentHandCard = createCreature(opponent, Components.Zone.HAND);
        int card = create("creatures/fire_spirit", player, Components.Zone.HAND);
        castFromHand(card, allyCreature);
        assertHasComponent(allyCreature, Components.Zone.GRAVEYARD);
        assertHasComponent(opponentHandCard, Components.Zone.GRAVEYARD);
    }
}
