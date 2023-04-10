package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFallOfTheFirstDragons extends TestGame {

    @Test
    public void testPutDragonsFromGraveyardToHandOnCast() {
        int[] dragons = createCreatures(2, player, Components.GRAVEYARD);
        forEach(dragons, dragon -> data.setComponent(dragon, Components.Tribe.DRAGON));
        int card = create("spells/fall_of_the_first_dragons", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(dragons, Components.HAND);
    }
}
