package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHardwareUpgrade extends TestGame {

    @Test
    public void testBuffMachineOnCast() {
        int machine = createVanilla(0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        data.setComponent(machine, Components.Tribe.MACHINE);
        int card = create("spells/hardware_upgrade", player, Components.Zone.HAND);
        castFromHand(card, machine);
        assertAttack(machine, 7);
        assertHealth(machine, 8);
    }
}
