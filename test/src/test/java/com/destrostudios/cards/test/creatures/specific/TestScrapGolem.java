package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestScrapGolem extends TestGame {

    @Test
    public void testSummonScrapPartsOnDeath() {
        int card = create("creatures/scrap_golem", player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Scrap Part", 3);
    }
}
