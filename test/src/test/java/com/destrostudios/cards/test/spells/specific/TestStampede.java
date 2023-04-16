package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestStampede extends TestGame {

    @Test
    public void testDestroyAllyBeastsAndAsManyOpponentCreatures() {
        int[] beasts = createCreatures(2, player, Components.Zone.CREATURE_ZONE);
        forEach(beasts, beast -> data.setComponent(beast, Components.Tribe.BEAST));
        int[] creatures = createCreatures(2, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("spells/stampede", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(beasts, Components.Zone.GRAVEYARD);
        assertHasComponent(creatures, Components.Zone.GRAVEYARD);
    }
}
