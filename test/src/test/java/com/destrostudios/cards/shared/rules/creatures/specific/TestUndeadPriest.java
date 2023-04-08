package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestUndeadPriest extends TestGame {

    @Test
    public void testDamageCreatureOnHeal() {
        int creature = createVanilla(0, 0, 5, opponent, Components.CREATURE_ZONE);
        damage(creature, 1);
        create("creatures/undead_priest", player, Components.CREATURE_ZONE);
        heal(creature, 1);
        assertHealthAndDamaged(creature, 1);
    }
}
