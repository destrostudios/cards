package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUndeadGeneral extends TestGame {

    @Test
    public void testIncreaseCreaturesManaCostWhenOnBoard() {
        int creature = createVanilla(1, 0, 1, player, Components.Zone.HAND);
        create("creatures/undead_general", player, Components.Zone.CREATURE_ZONE);
        assertManaCost(creature, 4);
    }
}
