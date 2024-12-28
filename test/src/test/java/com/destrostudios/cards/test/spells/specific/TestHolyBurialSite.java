package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHolyBurialSite extends TestGame {

    @Test
    public void testPutRandomCharactersAndSpellFromGraveyardToHandOnCast() {
        int[] creatures = createCreatures(2, player, Components.Zone.GRAVEYARD);
        int spell = createSpell(player, Components.Zone.GRAVEYARD);
        int card = create("spells/holy_burial_site", player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(creatures, Components.Zone.HAND);
        assertHasComponent(spell, Components.Zone.HAND);
    }
}
