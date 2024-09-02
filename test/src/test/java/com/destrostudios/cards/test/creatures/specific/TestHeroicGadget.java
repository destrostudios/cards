package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHeroicGadget extends TestGame {

    @Test
    public void testBuffMachineOnSummon() {
        int machine = createVanilla(0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        data.setComponent(machine, Components.Tribe.MACHINE);
        int card = create("creatures/heroic_gadget", player, Components.Zone.HAND);
        castFromHand(card, machine);
        assertAttack(machine, 2);
        endTurn(player);
        assertAttack(machine, 0);
    }
}
