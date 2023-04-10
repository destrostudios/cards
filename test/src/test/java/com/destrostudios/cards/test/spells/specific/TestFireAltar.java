package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFireAltar extends TestGame {

    @Test
    public void testDestroyRandomAllyCreaturesAndSummonDragonOnCast() {
        int[] creatures = createCreatures(2, player, Components.CREATURE_ZONE);
        int dragon = createCreature(player, Components.HAND);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int card = create("spells/fire_altar", player, Components.HAND);
        castFromHand(card, dragon);
        assertHasComponent(creatures, Components.GRAVEYARD);
        assertHasComponent(dragon, Components.CREATURE_ZONE);
    }
}
