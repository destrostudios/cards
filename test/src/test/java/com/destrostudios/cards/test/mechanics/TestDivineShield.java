package com.destrostudios.cards.test.mechanics;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDivineShield extends TestGame {

    @Test
    public void testActiveDivineShieldBlockingDamage() {
        int creature = createVanilla(0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        data.setComponent(creature, Components.Ability.DIVINE_SHIELD, true);
        damage(creature, 999);
        assertHealthAndUndamaged(creature, 1);
        assertComponent(creature, Components.Ability.DIVINE_SHIELD, false);
    }

    @Test
    public void testInactiveDivineShieldNotBlockingDamage() {
        int creature = createVanilla(0, 0, 1, player, Components.Zone.CREATURE_ZONE);
        data.setComponent(creature, Components.Ability.DIVINE_SHIELD, false);
        damage(creature, 999);
        assertHasComponent(creature, Components.Zone.GRAVEYARD);
    }
}
