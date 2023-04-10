package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestGideas extends TestGame {

    @Test
    public void testIncreaseOpponentCreaturesManaCostsWhenOnBoard() {
        int opponentCard = createVanilla(0, 0, 1, opponent, Components.HAND);
        create("creatures/gideas", player, Components.CREATURE_ZONE);
        assertManaCost(opponentCard, 1);
    }
}
