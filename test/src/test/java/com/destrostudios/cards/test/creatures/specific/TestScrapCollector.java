package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestScrapCollector extends TestGame {

    @Test
    public void testBuffOnAllyMachineDeath() {
        int card = create("creatures/scrap_collector", player, Components.Zone.CREATURE_ZONE);
        int machine = createCreature(player, Components.Zone.CREATURE_ZONE);
        data.setComponent(machine, Components.Tribe.MACHINE);
        destroy(machine);
        assertAttack(card, 4);
        assertHealth(card, 4);
    }
}
