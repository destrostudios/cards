package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestShrineSpecter extends TestGame {

    @Test
    public void testBuffOnPlaySpellCard() {
        int card = create("creatures/shrine_specter", player, Components.CREATURE_ZONE);
        int spell = createSpell(player, Components.HAND);
        castFromHand(spell);
        assertHealth(card, 3);
    }
}
