package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonmancer extends TestGame {

    @Test
    public void testDestroyAndReviveDragonOnDragonDestruction() {
        int dragon = createCreature(player, Components.Zone.CREATURE_ZONE);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int card = create("creatures/dragonmancer", player, Components.Zone.CREATURE_ZONE);
        destroy(dragon);
        assertHasComponent(card, Components.Zone.GRAVEYARD);
        assertHasComponent(dragon, Components.Zone.CREATURE_ZONE);
    }
}
