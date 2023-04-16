package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinAssaultSquad extends TestGame {

    @Test
    public void testSummonGoblinsOnCast() {
        int card = create("spells/goblin_assault_squad", player, Components.Zone.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.Zone.CREATURE_ZONE, "Goblin", 3);
    }
}
