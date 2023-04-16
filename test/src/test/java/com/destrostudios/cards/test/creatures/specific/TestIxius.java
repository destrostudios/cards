package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestIxius extends TestGame {

    @Test
    public void testDamageOnPlaySpellCard() {
        int[] creatures = createVanillas(2, 0, 0, 3, opponent, Components.Zone.CREATURE_ZONE);
        create("creatures/ixius", player, Components.Zone.CREATURE_ZONE);
        int spell = createSpell(player, Components.Zone.HAND);
        castFromHand(spell);
        assertHealthAndDamaged(creatures, 1);
    }
}
