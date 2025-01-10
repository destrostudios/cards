package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestTuningBot extends TestGame {

    @Test
    public void testDiscoverMachineAndBuffIt() {
        int card = create("creatures/tuning_bot", player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        int machine = getAndAssertFirstCard(player, Components.Zone.HAND, getDiscoveredCardName(DiscoverPool.MACHINE));
        assertAttack(machine, 3);
        assertHealth(machine, 5);
    }
}
