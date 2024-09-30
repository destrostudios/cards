package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSewerRat extends TestGame {

    @Test
    public void testSummonSewerRatFromLibraryOnDeath() {
        int otherCard = create("creatures/sewer_rat", player, Components.Zone.LIBRARY);
        int card = create("creatures/sewer_rat", player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        assertHasComponent(otherCard, Components.Zone.CREATURE_ZONE);
    }
}
