package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFrostSpirit extends TestGame {

    @Test
    public void testDestroyTwoRandomOpponentCreaturesWithTwoOrLessAttackOnSummon() {
        int allyCreature = createCreature(player, Components.Zone.CREATURE_ZONE);
        int opponentCreature1 = createVanilla(0, 2, 1, opponent, Components.Zone.CREATURE_ZONE);
        int opponentCreature2 = createVanilla(0, 2, 1, opponent, Components.Zone.CREATURE_ZONE);
        int opponentCreature3 = createVanilla(0, 3, 1, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/frost_spirit", player, Components.Zone.HAND);
        castFromHand(card, allyCreature);
        assertHasComponent(allyCreature, Components.Zone.GRAVEYARD);
        assertHasComponent(opponentCreature1, Components.Zone.GRAVEYARD);
        assertHasComponent(opponentCreature2, Components.Zone.GRAVEYARD);
        assertHasComponent(opponentCreature3, Components.Zone.CREATURE_ZONE);
    }
}
