package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBattery extends TestGame {

    @Test
    public void testHealOtherAllyMachinesOnTurnEnd() {
        int[] machines = createVanillas(2, 0, 0, 3, player, Components.Zone.CREATURE_ZONE);
        forEach(machines, machine -> {
            data.setComponent(machine, Components.Tribe.MACHINE);
            damage(machine, 2);
        });
        create("creatures/battery", player, Components.Zone.CREATURE_ZONE);
        endTurn(player);
        assertHealth(machines, 3);
    }
}
