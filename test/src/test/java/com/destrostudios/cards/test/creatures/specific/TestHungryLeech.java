package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHungryLeech extends TestGame {

    @Test
    public void testSummonFullLeechOnDamageDuringOwnTurn() {
        int card = create("creatures/hungry_leech", player, Components.Zone.CREATURE_ZONE);
        damage(player, 1);
        assertHasComponent(card, Components.Zone.GRAVEYARD);
        assertOneCard(player, Components.Zone.CREATURE_ZONE, "Full Leech");
    }
}
