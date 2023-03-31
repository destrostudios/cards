package com.destrostudios.cards.shared.rules.spells.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicSpellCreation extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/spells/basic_spell_creation.csv", numLinesToSkip = 1)
    public void testCreation(String template, String name, int manaCost) {
        int card = create(template, player);
        assertComponent(card, Components.NAME, name);
        assertManaCost(card, manaCost);
    }
}