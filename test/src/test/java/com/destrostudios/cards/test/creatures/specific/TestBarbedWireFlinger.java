package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBarbedWireFlinger extends TestGame {

    @Test
    public void testDamageSpell() {
        int target = createVanilla(0, 0, 2, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/barbed_wire_flinger", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, null, 2);
        cast(spell, target);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
        assertHealthAndDamaged(target, 1);
    }
}
