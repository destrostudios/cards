package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestBeerTap extends TestGame {

    @Test
    public void testDrawSpell() {
        int libraryCard = createCard(player, Components.Zone.LIBRARY);
        int card = create("creatures/beer_tap", player, Components.Zone.CREATURE_ZONE);
        int spell = getAndAssertSpell(card, 2, 2, 1);
        cast(spell);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 2);
        assertHasComponent(libraryCard, Components.Zone.HAND);
    }
}
