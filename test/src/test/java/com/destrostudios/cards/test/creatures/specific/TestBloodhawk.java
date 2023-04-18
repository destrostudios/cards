package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBloodhawk extends TestGame {

    @Test
    public void testBuffOnAllyBeastDeath() {
        int card = create("creatures/bloodhawk", player, Components.Zone.CREATURE_ZONE);
        int beast = createCreature(player, Components.Zone.CREATURE_ZONE);
        data.setComponent(beast, Components.Tribe.BEAST);
        destroy(beast);
        assertAttack(card, 7);
        assertHealth(card, 7);
    }
}
