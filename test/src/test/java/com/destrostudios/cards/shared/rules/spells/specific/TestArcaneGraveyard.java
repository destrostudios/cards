package com.destrostudios.cards.shared.rules.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestArcaneGraveyard extends TestGame {

    @Test
    public void testPutDragonsFromGraveyardToHandOnCast() {
        int[] spells = createSpells(2, player, Components.GRAVEYARD);
        int card = create("spells/arcane_graveyard", player, Components.HAND);
        castFromHand(card);
        assertHasComponent(spells, Components.HAND);
    }
}
