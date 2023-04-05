package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinCommander extends TestGame {

    @Test
    public void testBuffGoblinsWhenOnBoard() {
        int[] goblins = createVanillas(2, 0, 0, 1, player, Components.CREATURE_ZONE);
        forEach(goblins, goblin -> data.setComponent(goblin, Components.Tribe.GOBLIN));
        create("creatures/goblin_commander", player, Components.CREATURE_ZONE);
        assertAttack(goblins, 1);
        assertHealth(goblins, 2);
    }
}
