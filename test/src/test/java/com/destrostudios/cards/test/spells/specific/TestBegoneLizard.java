package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBegoneLizard extends TestGame {

    @Test
    public void testDestroyDragonOnCast() {
        int dragon = createCreature(opponent, Components.Zone.CREATURE_ZONE);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int card = create("spells/begone_lizard", player, Components.Zone.HAND);
        castFromHand(card, dragon);
        assertHasComponent(dragon, Components.Zone.GRAVEYARD);
    }
}
