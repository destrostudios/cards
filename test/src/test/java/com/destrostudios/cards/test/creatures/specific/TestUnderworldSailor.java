package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldSailor extends TestGame {

    @Test
    public void testSummonOnDiscard() {
        int card = create("creatures/underworld_sailor", player, Components.Zone.HAND);
        discard(card);
        assertHasComponent(card, Components.Zone.CREATURE_ZONE);
    }
}
