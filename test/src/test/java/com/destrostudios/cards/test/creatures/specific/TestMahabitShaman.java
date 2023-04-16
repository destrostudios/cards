package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestMahabitShaman extends TestGame {

    @Test
    public void testBuffBeastOnSummon() {
        int beast = createVanilla(0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        data.setComponent(beast, Components.Tribe.BEAST);
        int card = create("creatures/mahabit_shaman", player, Components.Zone.HAND);
        castFromHand(card, beast);
        assertAttack(beast, 2);
        assertHealth(beast, 3);
    }
}
