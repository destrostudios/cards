package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestSpikedGreyhump extends TestGame {

    @Test
    public void testDamageOpponentCreaturesOnDamageTaken() {
        int[] creatures = createVanillas(2, 0, 0, 2, opponent, Components.Zone.CREATURE_ZONE);
        int card = create("creatures/spiked_greyhump", player, Components.Zone.CREATURE_ZONE);
        damage(card, 1);
        assertHealthAndDamaged(creatures, 1);
    }
}
