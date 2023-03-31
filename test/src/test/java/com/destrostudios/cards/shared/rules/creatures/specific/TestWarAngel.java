package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestWarAngel extends TestGame {

    @Test
    public void testReduceCreaturesManaCostWhenOnBoard() {
        int creature = createVanilla(2, 1, 1, player, Components.HAND);
        create("creatures/war_angel", player, Components.CREATURE_ZONE);
        assertManaCost(creature, 1);
    }
}
