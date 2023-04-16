package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestFireAltar extends TestGame {

    @Test
    public void testDestroyRandomAllyCreaturesAndSummonDragonOnCast() {
        int[] creatures = createCreatures(2, player, Components.Zone.CREATURE_ZONE);
        int dragon = createCreature(player, Components.Zone.HAND);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int card = create("spells/fire_altar", player, Components.Zone.HAND);
        castFromHand(card, dragon);
        assertHasComponent(creatures, Components.Zone.GRAVEYARD);
        assertHasComponent(dragon, Components.Zone.CREATURE_ZONE);
    }
}
