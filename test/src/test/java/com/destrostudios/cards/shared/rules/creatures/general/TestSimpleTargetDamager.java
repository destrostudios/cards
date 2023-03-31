package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetDamager extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_target_damager.csv", numLinesToSkip = 1)
    public void testDamageOnSummon(String template, int damage) {
        int card = create(template, player, Components.HAND);
        int target = createVanilla( 0, 0, damage + 1, opponent, Components.CREATURE_ZONE);
        castFromHand(card, target);
        assertHealthAndDamaged(target, 1);
    }
}
