package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestOldAlchemist extends TestGame {

    @Test
    public void testPutSpellFromGraveyardToHandOnSummon() {
        int spell = createSpell(player, Components.GRAVEYARD);
        int card = create("creatures/old_alchemist", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(spell, Components.HAND);
    }
}
