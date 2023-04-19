package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestArtificialGuardian extends TestGame {

    @Test
    public void testBuffWhenOnBoard() {
        int card1 = create("creatures/artificial_guardian", player, Components.Zone.CREATURE_ZONE);
        assertAttack(card1, 2);
        assertHealth(card1, 1);
        int card2 = create("creatures/artificial_guardian", player, Components.Zone.CREATURE_ZONE);
        assertAttack(new int[] { card1, card2 }, 3);
        assertHealth(new int[] { card1, card2 }, 2);
    }
}
