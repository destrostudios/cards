package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldRot extends TestGame {

    @Test
    public void testDiscardTargetAndDestroyRandomOpponentCreatureOnCast() {
        int opponentCreature = createCreature(opponent, Components.Zone.CREATURE_ZONE);
        int target = createCard(player, Components.Zone.HAND);
        int card = create("spells/underworld_rot", player, Components.Zone.HAND);
        castFromHand(card, target);
        assertHasComponent(target, Components.Zone.GRAVEYARD);
        assertHasComponent(opponentCreature, Components.Zone.GRAVEYARD);
    }
}
