package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestElementalOrb extends TestGame {

    @Test
    public void testDamageAndSummonLandedElementalOrbOnCast() {
        int target = createVanilla(0, 0, 5, opponent, Components.CREATURE_ZONE);
        int card = create("spells/elemental_orb", player, Components.HAND);
        castFromHand(card, target);
        assertHealthAndDamaged(target, 1);
        assertOneCard(player, Components.CREATURE_ZONE, "Landed Elemental Orb");
    }
}
