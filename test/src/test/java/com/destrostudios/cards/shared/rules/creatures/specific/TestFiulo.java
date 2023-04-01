package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestFiulo extends TestGame {

    @Test
    public void testPutFromGraveyardToHandOnTurnEnd() {
        int card = create("creatures/fiulo", player, Components.GRAVEYARD);
        endTurn(player);
        assertHasComponent(card, Components.HAND);
    }
}
