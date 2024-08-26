package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldTempleStatue extends TestGame {

    @Test
    public void testBuffOpponentCreaturesOnDiscard() {
        int[] opponentCreatures = createVanillas(2, 0, 3, 0, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/underworld_temple_statue", player, Components.Zone.HAND);
        discard(card);
        assertAttack(opponentCreatures, 1);
        endTurn(player);
        assertAttack(opponentCreatures, 3);
    }
}
