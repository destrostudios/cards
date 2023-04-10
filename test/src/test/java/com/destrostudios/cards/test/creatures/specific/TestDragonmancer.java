package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonmancer extends TestGame {

    @Test
    public void testDestroyAndReviveDragonOnDragonDestruction() {
        int dragon = createCreature(player, Components.CREATURE_ZONE);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int card = create("creatures/dragonmancer", player, Components.CREATURE_ZONE);
        destroy(dragon);
        assertHasComponent(card, Components.GRAVEYARD);
        assertHasComponent(dragon, Components.CREATURE_ZONE);
    }
}
