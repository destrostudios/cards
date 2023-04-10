package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonRider extends TestGame {

    @Test
    public void testBuffOnSummonWhenDragonInHand() {
        int dragon = createCreature(player, Components.HAND);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int card = create("creatures/dragon_rider", player, Components.HAND);
        castFromHand(card);
        assertAttack(card, 4);
        assertHealth(card, 5);
    }
}
