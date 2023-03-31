package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicCreation extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_creation.csv")
    public void testCreation(String template, String name, int manaCost, int attack, int health) {
        int card = create(template, player);
        assertComponent(card, Components.NAME, name);
        assertManaCost(card, manaCost);
        assertAttack(card, attack);
        assertHealth(card, health);
    }
}
