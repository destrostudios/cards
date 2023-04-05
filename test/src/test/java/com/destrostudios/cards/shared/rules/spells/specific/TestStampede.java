package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestStampede extends TestGame {

    @Test
    public void testDestroyAllyBeastsAndAsManyOpponentCreatures() {
        int[] beasts = createCreatures(2, player, Components.CREATURE_ZONE);
        forEach(beasts, beast -> data.setComponent(beast, Components.Tribe.BEAST));
        int[] creatures = createCreatures(2, opponent, Components.CREATURE_ZONE);
        int card = create("spells/stampede", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(beasts, Components.GRAVEYARD);
        assertHasComponent(creatures, Components.GRAVEYARD);
    }
}
