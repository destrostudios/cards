package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestPlague extends TestGame {

    @Test
    public void testDamageOwnerAndDestroyAllCreaturesOnCast() {
        int[] creatures = createCreaturesForBothPlayers(2, Components.Zone.CREATURE_ZONE);
        int card = create("spells/plague", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 8);
        assertHasComponent(creatures, Components.Zone.GRAVEYARD);
    }
}
