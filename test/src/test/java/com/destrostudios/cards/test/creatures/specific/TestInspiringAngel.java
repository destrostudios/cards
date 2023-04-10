package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestInspiringAngel extends TestGame {

    @Test
    public void testBuffCreaturesOnSummon() {
        int[] creatures = createVanillas(2, 0, 1, 1, player, Components.CREATURE_ZONE);
        int card = create("creatures/inspiring_angel", player, Components.HAND);
        castFromHand(card);
        assertAttack(creatures, 2);
        assertHealth(creatures, 2);
    }
}
