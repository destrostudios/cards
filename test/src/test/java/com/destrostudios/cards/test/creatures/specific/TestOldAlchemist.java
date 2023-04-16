package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestOldAlchemist extends TestGame {

    @Test
    public void testPutSpellFromGraveyardToHandOnSummon() {
        int spell = createSpell(player, Components.Zone.GRAVEYARD);
        int card = create("creatures/old_alchemist", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(spell, Components.Zone.HAND);
    }
}
