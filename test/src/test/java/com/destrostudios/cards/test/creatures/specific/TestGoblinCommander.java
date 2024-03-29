package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinCommander extends TestGame {

    @Test
    public void testBuffGoblinsWhenOnBoard() {
        int[] goblins = createVanillas(2, 0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        forEach(goblins, goblin -> data.setComponent(goblin, Components.Tribe.GOBLIN));
        create("creatures/goblin_commander", player, Components.Zone.CREATURE_ZONE);
        assertAttack(goblins, 1);
        assertHealth(goblins, 2);
    }
}
