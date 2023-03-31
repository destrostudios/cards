package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestKhazarMerchant extends TestGame {

    @Test
    public void testDrawSpellOnSummon() {
        int spell = createSpell(3, player, Components.LIBRARY);
        int card = create("creatures/khazar_merchant", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(spell, Components.HAND);
    }
}