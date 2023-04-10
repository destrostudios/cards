package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestTheKingsArmy extends TestGame {

    @Test
    public void testSummonKingsSoldiersOnCast() {
        int card = create("spells/the_kings_army", player, Components.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.CREATURE_ZONE, "King's Soldier", 2);
    }
}
