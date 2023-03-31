package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetStatsBuffer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_target_stats_buffer.csv")
    public void testBuffOnSummon(String template, int bonusAttack, int bonusHealth) {
        int card = create(template, player, Components.HAND);
        int target = createVanilla( 0, 1, 1, player, Components.CREATURE_ZONE);
        castFromHand(card, target);
        assertAttack(target, 1 + bonusAttack);
        assertHealth(target, 1 + bonusHealth);
    }
}
