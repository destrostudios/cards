package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestUnderworldGhoul extends TestGame {

    @Test
    public void testDiscardAndGainAttackSpell() {
        int target = createCard(player, Components.Zone.HAND);
        int card = create("creatures/underworld_ghoul", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, null, 1);
        cast(spell, target);
        assertHasComponent(target, Components.Zone.GRAVEYARD);
        assertAttack(card, 6);
    }
}
