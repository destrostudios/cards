package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestArcaneGraveyard extends TestGame {

    @Test
    public void testPutDragonsFromGraveyardToHandOnCast() {
        int[] spells = createSpells(2, player, Components.Zone.GRAVEYARD);
        int card = create("spells/arcane_graveyard", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(spells, Components.Zone.HAND);
    }
}
