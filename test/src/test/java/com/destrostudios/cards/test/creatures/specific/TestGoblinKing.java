package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinKing extends TestGame {

    @Test
    public void testBuffOnSummon() {
        int[] goblins = createCreatures(2, player, Components.Zone.CREATURE_ZONE);
        forEach(goblins, goblin -> data.setComponent(goblin, Components.Tribe.GOBLIN));
        int card = create("creatures/goblin_king", player, Components.Zone.HAND);
        castFromHand(card);
        assertAttack(card, 6);
    }
}
