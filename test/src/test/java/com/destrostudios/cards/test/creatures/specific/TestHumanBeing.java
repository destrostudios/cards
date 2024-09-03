package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestHumanBeing extends TestGame {

    @Test
    public void testHealCharactersOnSummon() {
        int[] creatures = createVanillasForBothPlayers(2, 0, 0, 3, Components.Zone.CREATURE_ZONE);
        damage(player, 2);
        damage(opponent, 2);
        damage(creatures, 2);
        int card = create("creatures/human_being", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
        assertHealthAndDamaged(opponent, GameConstants.PLAYER_HEALTH - 1);
        assertHealthAndDamaged(creatures, 2);
    }
}
