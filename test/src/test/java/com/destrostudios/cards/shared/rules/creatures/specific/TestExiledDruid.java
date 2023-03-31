package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestExiledDruid extends TestGame {

    @Test
    public void testReviveBeastOnSummon() {
        int beast = createVanilla(0, 1, 1, player, Components.GRAVEYARD);
        data.setComponent(beast, Components.Tribe.BEAST);
        int card = create("creatures/exiled_druid", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(beast, Components.CREATURE_ZONE);
    }
}
