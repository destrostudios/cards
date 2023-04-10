package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetHealer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_target_healer.csv", numLinesToSkip = 1)
    public void testHealOnSummon(String template, int heal) {
        int target = createVanilla(0, 0, heal + 2, opponent, Components.CREATURE_ZONE);
        int card = create(template, player, Components.HAND);
        damage(target, heal + 1);
        castFromHand(card, target);
        assertHealthAndDamaged(target, 1 + heal);
    }
}
