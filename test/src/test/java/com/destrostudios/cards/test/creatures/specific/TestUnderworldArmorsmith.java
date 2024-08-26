package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldArmorsmith extends TestGame {

    @Test
    public void testDiscardAndGainHealthSpell() {
        int target = createCard(player, Components.Zone.HAND);
        int card = create("creatures/underworld_armorsmith", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, 1, 1);
        cast(spell, target);
        assertHasComponent(target, Components.Zone.GRAVEYARD);
        assertHealth(card, 7);
    }
}
