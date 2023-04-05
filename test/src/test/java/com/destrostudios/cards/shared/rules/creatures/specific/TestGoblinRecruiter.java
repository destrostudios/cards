package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinRecruiter extends TestGame {

    @Test
    public void testSummonGoblinSpell() {
        int card = create("creatures/goblin_recruiter", player, Components.CREATURE_ZONE);
        int spell = data.getComponent(card, Components.SPELLS)[2];
        assertManaCostSpell(spell, 2);
        cast(spell);
        assertCardsCount(player, Components.CREATURE_ZONE, "Goblin", 1);
    }
}
