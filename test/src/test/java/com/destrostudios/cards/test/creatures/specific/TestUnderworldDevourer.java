package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldDevourer extends TestGame {

    @Test
    public void testBuffOnAllyDiscard() {
        int target = createCard(player, Components.Zone.HAND);
        int card = create("creatures/underworld_devourer", player, Components.Zone.CREATURE_ZONE);
        discard(target);
        assertAttack(card, 4);
        assertHealth(card, 3);
    }
}
