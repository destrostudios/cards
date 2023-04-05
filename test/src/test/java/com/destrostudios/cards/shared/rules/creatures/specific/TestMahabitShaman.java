package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestMahabitShaman extends TestGame {

    @Test
    public void testBuffBeastOnSummon() {
        int beast = createVanilla(0, 0, 1, player, Components.CREATURE_ZONE);
        data.setComponent(beast, Components.Tribe.BEAST);
        int card = create("creatures/mahabit_shaman", player, Components.HAND);
        castFromHand(card, beast);
        assertAttack(beast, 2);
        assertHealth(beast, 3);
    }
}
