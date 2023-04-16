package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHolyLegionLeader extends TestGame {

    @Test
    public void testBuffOnHeal() {
        int creature = createVanilla(0, 0, 2, player, Components.Zone.CREATURE_ZONE);
        damage(creature, 1);
        int card = create("creatures/holy_legion_leader", player, Components.Zone.CREATURE_ZONE);
        heal(creature, 1);
        assertAttack(card, 5);
        assertHealth(card, 5);
    }
}
