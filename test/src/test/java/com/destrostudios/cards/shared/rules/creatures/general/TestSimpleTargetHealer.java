package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetHealer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_target_healer.csv", numLinesToSkip = 1)
    public void testHealOnSummon(String template, int heal) {
        int card = create(template, player, Components.HAND);
        int target = createVanilla( 0, 0, heal + 2, opponent, Components.CREATURE_ZONE);
        damage(target, heal + 1);
        castFromHand(card, target);
        assertHealthAndDamaged(target, 1 + heal);
    }
}
