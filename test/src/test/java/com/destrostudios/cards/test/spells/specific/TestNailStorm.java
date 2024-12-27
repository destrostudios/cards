package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestNailStorm extends TestGame {

    @Test
    public void testDamageCharactersOnCast() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 0, 2, Components.Zone.CREATURE_ZONE);
        int card = create("spells/nail_storm", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 1);
        assertHealthAndDamaged(players, GameConstants.PLAYER_HEALTH - 1);
    }
}
