package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestSunOrnamentDragon extends TestGame {

    @Test
    public void testHealOnSummon() {
        damage(player, 5);
        int[] dragons = createCreatures(2, player, Components.HAND);
        forEach(dragons, dragon -> data.setComponent(dragon, Components.Tribe.DRAGON));
        int card = create("creatures/sun_ornament_dragon", player, Components.HAND);
        castFromHand(card);
        assertHealthAndDamaged(player, GameConstants.PLAYER_HEALTH - 1);
    }
}
