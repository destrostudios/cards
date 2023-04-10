package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestLeaderOfTheSceleratis extends TestGame {

    @Test
    public void testDamageOnPlayCard() {
        int handCard = createCard(player, Components.HAND);
        create("creatures/leader_of_the_sceleratis", player, Components.CREATURE_ZONE);
        castFromHand(handCard);
        assertHealthAndDamaged(opponent, GameConstants.PLAYER_HEALTH - 1);
    }
}
