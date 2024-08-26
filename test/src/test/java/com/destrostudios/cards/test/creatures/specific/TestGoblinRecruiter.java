package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestGoblinRecruiter extends TestGame {

    @Test
    public void testSummonGoblinSpell() {
        int card = create("creatures/goblin_recruiter", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, 2, null);
        cast(spell);
        assertOneCard(player, Components.Zone.CREATURE_ZONE, "Goblin");
    }
}
