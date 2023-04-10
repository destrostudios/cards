package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicAttack extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_attack.csv", numLinesToSkip = 1)
    public void testAttack(String template, int timesPerTurn) {
        int card = create(template, player, Components.CREATURE_ZONE);
        int target = createVanilla(0, 0, getAttack(card) + 1, opponent, Components.CREATURE_ZONE);
        int defaultAttackSpell = getDefaultAttackSpell(card);
        assertComponent(defaultAttackSpell, Components.Spell.MAXIMUM_CASTS_PER_TURN, timesPerTurn);
        cast(defaultAttackSpell, target);
        assertHealth(target, 1);
    }
}
