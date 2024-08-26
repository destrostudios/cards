package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonEgg extends TestGame {

    @Test
    public void testHatchIntoDragonSpell() {
        int card = create("creatures/dragon_egg", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, 3, null);
        cast(spell);
        assertHasComponent(card, Components.Zone.GRAVEYARD);
        assertOneCard(player, Components.Zone.CREATURE_ZONE, "Dragon");
    }
}
