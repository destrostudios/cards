package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestCloudOfRevenge extends TestGame {

    @Test
    public void testDestroyRandomOpponentCreatureOnSummonWhenLargeHealthDifference() {
        int opponentCreature = createCreature(opponent, Components.Zone.CREATURE_ZONE);
        damage(player, 10);
        int card = create("creatures/cloud_of_revenge", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(opponentCreature, Components.Zone.GRAVEYARD);
    }

    @Test
    public void testDestroyNoCreatureOnSummonWhenLowHealthDifference() {
        int opponentCreature = createCreature(opponent, Components.Zone.CREATURE_ZONE);
        damage(player, 9);
        int card = create("creatures/cloud_of_revenge", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(opponentCreature, Components.Zone.CREATURE_ZONE);
    }
}
