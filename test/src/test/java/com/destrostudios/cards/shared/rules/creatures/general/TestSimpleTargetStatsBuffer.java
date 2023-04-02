package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetStatsBuffer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_target_stats_buffer.csv", numLinesToSkip = 1)
    public void testBuffOnSummon(String template, Integer bonusAttack, Integer bonusHealth, Integer setAttack, Integer setHealth) {
        int target = createVanilla(0, 1, 1, player, Components.CREATURE_ZONE);
        int card = create(template, player, Components.HAND);
        castFromHand(card, target);
        assertAttack(target, ((setAttack != null) ? setAttack : 1 + ((bonusAttack != null) ? bonusAttack : 0)));
        assertHealth(target, ((setHealth != null) ? setHealth : 1 + ((bonusHealth != null) ? bonusHealth : 0)));
    }
}
