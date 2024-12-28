package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestVendingMachine extends TestGame {

    @Test
    public void testGainTributedMachineStatsOnSummon() {
        int machine = createVanilla(1, 1, 1, player, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/vending_machine", player, Components.Zone.HAND);
        castFromHand(card, machine);
        assertHasComponent(machine, Components.Zone.GRAVEYARD);
        assertAttack(card, 4);
        assertHealth(card, 4);
    }
}
