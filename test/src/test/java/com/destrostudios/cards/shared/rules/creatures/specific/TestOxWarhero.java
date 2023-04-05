package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestOxWarhero extends TestGame {

    @Test
    public void testBuffWhenOtherAllyBeastOnBoard() {
        int beast = createVanilla(0, 0, 1, player, Components.CREATURE_ZONE);
        data.setComponent(beast, Components.Tribe.BEAST);
        int card = create("creatures/ox_warhero", player, Components.CREATURE_ZONE);
        assertHealth(card, 3);
    }
}
