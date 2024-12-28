package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestOverclock extends TestGame {

    @Test
    public void testBuffMachinesAndDestroyThemAtEndOfTurnOnCast() {
        int[] machines = createVanillas(2, 0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        forEach(machines, machine -> data.setComponent(machine, Components.Tribe.MACHINE));
        int card = create("spells/overclock", player, Components.Zone.HAND);
        castFromHand(card);
        assertAttack(machines, 3);
        endTurn(player);
        assertHasComponent(machines, Components.Zone.GRAVEYARD);
    }
}
