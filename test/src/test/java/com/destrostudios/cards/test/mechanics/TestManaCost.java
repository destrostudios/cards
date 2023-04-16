package com.destrostudios.cards.test.mechanics;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestManaCost extends TestGame {

    @Test
    public void testPayManaCostOnCast() {
        setFullMana(player, 3);
        int spell = createSpell(2, player, Components.Zone.HAND);
        castFromHand(spell);
        assertComponent(player, Components.MANA, 1);
    }
}
