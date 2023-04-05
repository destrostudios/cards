package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestDragonEgg extends TestGame {

    @Test
    public void testHatchIntoDragonSpell() {
        int card = create("creatures/dragon_egg", player, Components.CREATURE_ZONE);
        int spell = data.getComponent(card, Components.SPELLS)[2];
        assertManaCostSpell(spell, 3);
        cast(spell);
        assertHasComponent(card, Components.GRAVEYARD);
        assertCardsCount(player, Components.CREATURE_ZONE, "Dragon", 1);
    }
}
