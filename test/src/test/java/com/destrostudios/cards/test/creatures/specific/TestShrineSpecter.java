package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestShrineSpecter extends TestGame {

    @Test
    public void testBuffOnPlaySpellCard() {
        int card = create("creatures/shrine_specter", player, Components.Zone.CREATURE_ZONE);
        int spell = createSpell(player, Components.Zone.HAND);
        castFromHand(spell);
        assertHealth(card, 3);
    }
}
