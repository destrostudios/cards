package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonsCounterattack extends TestGame {

    @Test
    public void testDiscardDragonAndDamageOpponentCreaturesOnCast() {
        int dragon = createCreature(player, Components.HAND);
        data.setComponent(dragon, Components.Tribe.DRAGON);
        int[] creatures = createVanillas(2, 0, 0, 7, opponent, Components.CREATURE_ZONE);
        int card = create("spells/dragons_counterattack", player, Components.HAND);
        castFromHand(card, dragon);
        assertHasComponent(dragon, Components.GRAVEYARD);
        assertHealthAndDamaged(creatures, 1);
    }
}
