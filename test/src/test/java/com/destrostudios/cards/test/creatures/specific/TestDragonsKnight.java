package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonsKnight extends TestGame {

    @Test
    public void testReduceDragonsManaCostWhenOnBoard() {
        int dragon = createVanilla(3, 0, 1, player, Components.HAND);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        create("creatures/dragons_knight", player, Components.CREATURE_ZONE);
        assertManaCost(dragon, 1);
    }
}
