package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestBloodhawk extends TestGame {

    @Test
    public void testBuffOnAllyBeastDeath() {
        int card = create("creatures/bloodhawk", player, Components.CREATURE_ZONE);
        int beast = createCreature(player, Components.CREATURE_ZONE);
        data.setComponent(beast, Components.Tribe.BEAST);
        destroy(beast);
        assertAttack(card, 6);
        assertHealth(card, 6);
    }
}
