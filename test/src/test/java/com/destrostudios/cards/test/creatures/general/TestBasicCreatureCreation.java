package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicCreatureCreation extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_creature_creation.csv", numLinesToSkip = 1)
    public void testCreation(String template, String name, boolean legendary, int manaCost, int attack, int health, boolean taunt, boolean divineShield) {
        int card = create(template, player);
        assertComponent(card, Components.NAME, name);
        assertComponentExistence(card, Components.LEGENDARY, legendary);
        assertManaCost(card, manaCost);
        assertAttack(card, attack);
        assertHealth(card, health);
        assertComponentExistence(card, Components.Ability.TAUNT, taunt);
        assertComponentExistence(card, Components.Ability.DIVINE_SHIELD, divineShield);
    }
}
