package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHarannaUdagan extends TestGame {

    @Test
    public void testDrawOnDeath() {
        int libraryCard = createCard(player, Components.LIBRARY);
        int card = create("creatures/haranna_udagan", player, Components.CREATURE_ZONE);
        destroy(card);
        assertHasComponent(libraryCard, Components.HAND);
    }
}
