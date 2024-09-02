package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestRecycling extends TestGame {

    @Test
    public void testShuffleMachinesFromGraveyardIntoLibraryAndDrawOnCast() {
        int[] machines = createCreatures(3, player, Components.Zone.GRAVEYARD);
        forEach(machines, machine -> data.setComponent(machine, Components.Tribe.MACHINE));
        int card = create("spells/recycling", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(machines, Components.Zone.HAND);
    }
}
