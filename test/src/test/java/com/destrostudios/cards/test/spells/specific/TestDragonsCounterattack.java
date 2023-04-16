package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonsCounterattack extends TestGame {

    @Test
    public void testDiscardDragonAndDamageOpponentCreaturesOnCast() {
        int dragon = createCreature(player, Components.Zone.HAND);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int[] creatures = createVanillas(2, 0, 0, 7, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("spells/dragons_counterattack", player, Components.Zone.HAND);
        castFromHand(card, dragon);
        assertHasComponent(dragon, Components.Zone.GRAVEYARD);
        assertHealthAndDamaged(creatures, 1);
    }
}
