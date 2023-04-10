package com.destrostudios.cards.test.spells.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSeaStorm extends TestGame {

    @Test
    public void testDamageCharactersOnCast() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 0, 4, Components.CREATURE_ZONE);
        int card = create("spells/sea_storm", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(creatures, 1);
        assertHealthAndDamaged(players, GameConstants.PLAYER_HEALTH - 3);
    }
}
