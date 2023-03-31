package com.destrostudios.cards.shared.rules.creatures.custom;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonsKnight extends TestGame {

    @Test
    public void testReduceDragonsManaCostWhenOnBoard() {
        int dragon = createVanilla(3, 1, 1, player, Components.HAND);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        assertManaCost(dragon, 3);
        create("creatures/dragons_knight", player, Components.CREATURE_ZONE);
        assertManaCost(dragon, 1);
    }
}
