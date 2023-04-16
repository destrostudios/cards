package com.destrostudios.cards.test.spells.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTargetDamage extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/spells/simple_target_damage.csv", numLinesToSkip = 1)
    public void testDamageOnCast(String template, int damage) {
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card, opponent);
        assertHealthAndDamaged(opponent, GameConstants.PLAYER_HEALTH - damage);
    }
}
