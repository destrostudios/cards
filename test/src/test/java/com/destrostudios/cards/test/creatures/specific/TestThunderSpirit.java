package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestThunderSpirit extends TestGame {

    @Test
    public void testDestroyRandomOpponentCreatureOnSummon() {
        int allyCreature = createCreature(player, Components.Zone.CREATURE_ZONE);
        int opponentCreature = createCreature(opponent, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/thunder_spirit", player, Components.Zone.HAND);
        castFromHand(card, allyCreature);
        assertHasComponent(allyCreature, Components.Zone.GRAVEYARD);
        assertHasComponent(opponentCreature, Components.Zone.GRAVEYARD);
    }
}
