package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBonespiritFenrir extends TestGame {

    @Test
    public void testBuffWhenOtherAllyCreatureOnBoard() {
        int creature = createCreature(player, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/bonespirit_fenrir", player, Components.Zone.CREATURE_ZONE);
        assertHealth(card, 1);
        destroy(creature);
        assertHealth(card, 0);
        assertHasComponent(card, Components.Zone.GRAVEYARD);
    }
}
