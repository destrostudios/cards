package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestRelicGuardians extends TestGame {

    @Test
    public void testSummonRelicGuardiansOnCast() {
        int card = create("spells/relic_guardians", player, Components.Zone.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Relic Guardian", 2);
    }
}
