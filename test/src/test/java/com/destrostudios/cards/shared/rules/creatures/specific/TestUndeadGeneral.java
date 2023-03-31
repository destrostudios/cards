package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestUndeadGeneral extends TestGame {

    @Test
    public void testIncreaseCreaturesManaCostWhenOnBoard() {
        int creature = createVanilla(1, 1, 1, player, Components.HAND);
        create("creatures/undead_general", player, Components.CREATURE_ZONE);
        assertManaCost(creature, 4);
    }
}
