package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestAngelicGate extends TestGame {

    @Test
    public void testHealSpell() {
        int target = createVanilla(0, 0, 3, player, Components.CREATURE_ZONE);
        damage(target, 2);
        int card = create("creatures/angelic_gate", player, Components.CREATURE_ZONE);
        int spell = data.getComponent(card, Components.SPELLS)[2];
        assertManaCostSpell(spell, 1);
        cast(spell, target);
        assertHealthAndDamaged(target, 2);
    }
}
