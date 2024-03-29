package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestAngelicGate extends TestGame {

    @Test
    public void testHealSpell() {
        int target = createVanilla(0, 0, 3, player, Components.Zone.CREATURE_ZONE);
        damage(target, 2);
        int card = create("creatures/angelic_gate", player, Components.Zone.CREATURE_ZONE);
        int spell = data.getComponent(card, Components.SPELLS)[2];
        assertManaCostSpell(spell, 1);
        cast(spell, target);
        assertHealthAndDamaged(target, 2);
    }
}
