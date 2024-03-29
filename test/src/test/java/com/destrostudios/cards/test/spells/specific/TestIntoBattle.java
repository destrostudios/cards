package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestIntoBattle extends TestGame {

    @Test
    public void testBuffHandCreaturesOnCast() {
        int[] creatures = createVanillas(2, 0, 1, 1, player, Components.Zone.HAND);
        int card = create("spells/into_battle", player, Components.Zone.HAND);
        castFromHand(card);
        assertAttack(creatures, 3);
        assertHealth(creatures, 3);
    }
}
