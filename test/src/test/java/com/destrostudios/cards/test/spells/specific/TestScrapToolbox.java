package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestScrapToolbox extends TestGame {

    @Test
    public void testSummonMachinesOnCast() {
        int card = create("spells/scrap_toolbox", player, Components.Zone.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Scrap Part", 3);
    }
}
