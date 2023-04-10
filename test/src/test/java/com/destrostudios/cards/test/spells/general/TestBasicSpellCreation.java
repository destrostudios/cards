package com.destrostudios.cards.test.spells.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicSpellCreation extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/spells/basic_spell_creation.csv", numLinesToSkip = 1)
    public void testCreation(String template, String name, boolean legendary, int manaCost) {
        int card = create(template, player);
        assertComponent(card, Components.NAME, name);
        assertComponentExistence(card, Components.LEGENDARY, legendary);
        assertManaCost(card, manaCost);
    }
}
