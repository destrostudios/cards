package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicAttack extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_attack.csv", numLinesToSkip = 1)
    public void testAttack(String template) {
        int card = create(template, player, Components.CREATURE_ZONE);
        int target = createVanilla(0, 0, getAttack(card) + 1, opponent, Components.CREATURE_ZONE);
        attack(card, target);
        assertHealth(target, 1);
    }
}
