package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinAssaultSquad extends TestGame {

    @Test
    public void testSummonGoblinsOnCast() {
        int card = create("spells/goblin_assault_squad", player, Components.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.CREATURE_ZONE, "Goblin", 3);
    }
}
