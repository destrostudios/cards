package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestMedicalSupplyWaggon extends TestGame {

    @Test
    public void testHealOnDamageTaken() {
        create("creatures/medical_supply_waggon", player, Components.Zone.CREATURE_ZONE);
        damage(player, 3);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
    }
}
