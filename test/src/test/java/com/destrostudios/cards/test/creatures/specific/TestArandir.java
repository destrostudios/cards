package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestArandir extends TestGame {

    @Test
    public void testDamageSpell() {
        int target = createVanilla(0, 0, 2, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/arandir", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, 1, 1);
        cast(spell, target);
        assertHealthAndDamaged(target, 1);
    }
}
