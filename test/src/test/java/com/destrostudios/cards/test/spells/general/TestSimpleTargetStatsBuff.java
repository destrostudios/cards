package com.destrostudios.cards.test.spells.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetStatsBuff extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/spells/simple_target_stats_buff.csv", numLinesToSkip = 1)
    public void testBuffOnCast(String template, int bonusAttack, int bonusHealth) {
        int target = createVanilla(0, 1, 1, player, Components.Zone.CREATURE_ZONE);
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card, target);
        assertAttack(target, 1 + bonusAttack);
        assertHealth(target, 1 + bonusHealth);
    }
}
