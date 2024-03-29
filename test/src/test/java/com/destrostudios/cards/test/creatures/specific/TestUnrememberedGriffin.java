package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnrememberedGriffin extends TestGame {

    @Test
    public void testDamageCreaturesOnSummon() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 0, 2, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/unremembered_griffin", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 1);
    }
}
