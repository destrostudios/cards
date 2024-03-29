package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestArandir extends TestGame {

    @Test
    public void testDamageSpell() {
        int target = createVanilla(0, 0, 2, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/arandir", player, Components.Zone.CREATURE_ZONE);
        int spell = data.getComponent(card, Components.SPELLS)[2];
        assertManaCostSpell(spell, 1);
        assertComponent(spell, Components.Spell.MAXIMUM_CASTS_PER_TURN, 1);
        cast(spell, target);
        assertHealthAndDamaged(target, 1);
    }
}
