package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestTheEtherDragon extends TestGame {

    @Test
    public void testSummonDragonsAndEndTurnOnSummon() {
        int[] dragons = createCreatures(2, player, Components.HAND);
        forEach(dragons, dragon -> data.setComponent(dragon, Components.Tribe.DRAGON));
        int card = create("creatures/the_ether_dragon", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(dragons, Components.CREATURE_ZONE);
        assertHasComponent(opponent, Components.Player.ACTIVE_PLAYER);
    }
}
