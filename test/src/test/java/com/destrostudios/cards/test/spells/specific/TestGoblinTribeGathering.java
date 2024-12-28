package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinTribeGathering extends TestGame {

    @Test
    public void testShuffleGoblinsFromGraveyardIntoLibraryAndDrawOnCast() {
        int[] goblins = createCreatures(2, player, Components.Zone.GRAVEYARD);
        forEach(goblins, machine -> data.setComponent(machine, Components.Tribe.GOBLIN));
        int card = create("spells/goblin_tribe_gathering", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(goblins, Components.Zone.HAND);
    }
}
