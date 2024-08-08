package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestExiledDruid extends TestGame {

    @Test
    public void testReviveBeastOnSummon() {
        int beast = createCreature(player, Components.Zone.GRAVEYARD);
        data.setComponent(beast, Components.Tribe.BEAST);
        int card = create("creatures/exiled_druid", player, Components.Zone.HAND);
        castFromHand(card, beast);
        assertHasComponent(beast, Components.Zone.CREATURE_ZONE);
    }
}
