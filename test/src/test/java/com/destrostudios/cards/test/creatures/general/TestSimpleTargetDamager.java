package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetDamager extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_target_damager.csv", numLinesToSkip = 1)
    public void testDamageOnSummon(String template, int damage) {
        int target = createVanilla(0, 0, damage + 1, opponent, Components.Zone.CREATURE_ZONE);
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card, target);
        assertHealthAndDamaged(target, 1);
    }
}
