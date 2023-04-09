package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestBonespiritFenrir extends TestGame {

    @Test
    public void testBuffWhenOtherAllyCreatureOnBoard() {
        int creature = createCreature(player, Components.CREATURE_ZONE);
        int card = create("creatures/bonespirit_fenrir", player, Components.CREATURE_ZONE);
        assertHealth(card, 1);
        destroy(creature);
        assertHealth(card, 0);
        assertHasComponent(card, Components.GRAVEYARD);
    }
}
