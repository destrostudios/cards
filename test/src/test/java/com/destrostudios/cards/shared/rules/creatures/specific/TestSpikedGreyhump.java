package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestSpikedGreyhump extends TestGame {

    @Test
    public void testDamageOpponentCreaturesOnDamageTaken() {
        int[] creatures = createVanillas(2, 0, 0, 2, opponent, Components.CREATURE_ZONE);
        int card = create("creatures/spiked_greyhump", player, Components.CREATURE_ZONE);
        damage(card, 1);
        assertHealthAndDamaged(creatures, 1);
    }
}
