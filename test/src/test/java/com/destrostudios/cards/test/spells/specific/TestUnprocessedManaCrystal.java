package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnprocessedManaCrystal extends TestGame {

    @Test
    public void testGainManaOnCast() {
        int card = create("spells/unprocessed_mana_crystal", player, Components.Zone.HAND);
        castFromHand(card);
        assertComponent(player, Components.MANA, 2);
    }
}
