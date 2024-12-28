package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSunOrnamentDragon extends TestGame {

    @Test
    public void testHealOnSummon() {
        damage(player, 7);
        int[] dragons = createCreatures(2, player, Components.Zone.HAND);
        forEach(dragons, dragon -> data.setComponent(dragon, Components.Tribe.DRAGON));
        int card = create("creatures/sun_ornament_dragon", player, Components.Zone.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
    }
}
