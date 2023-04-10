package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestWarcry extends TestGame {

    @Test
    public void testBuffGoblinsOnCast() {
        int[] goblins = createVanillas(2, 0, 1, 1, player, Components.CREATURE_ZONE);
        forEach(goblins, goblin -> data.setComponent(goblin, Components.Tribe.GOBLIN));
        int card = create("spells/warcry", player, Components.HAND);
        castFromHand(card);
        assertAttack(goblins, 2);
        endTurn(player);
        assertAttack(goblins, 1);
    }
}
