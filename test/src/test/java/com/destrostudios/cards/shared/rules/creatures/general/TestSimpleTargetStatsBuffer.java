package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetStatsBuffer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_target_stats_buffer.csv", numLinesToSkip = 1)
    public void testBuffOnSummon(String template, int bonusAttack, int bonusHealth) {
        int target = createVanilla(0, 1, 1, player, Components.CREATURE_ZONE);
        int card = create(template, player, Components.HAND);
        castFromHand(card, target);
        assertAttack(target, 1 + bonusAttack);
        assertHealth(target, 1 + bonusHealth);
    }
}