package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinPillager extends TestGame {

    @Test
    public void testDrawGoblinOnSummon() {
        int goblin = createCreature(player, Components.LIBRARY);
        data.setComponent(goblin, Components.Tribe.GOBLIN);
        int card = create("creatures/goblin_pillager", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(goblin, Components.HAND);
    }
}
