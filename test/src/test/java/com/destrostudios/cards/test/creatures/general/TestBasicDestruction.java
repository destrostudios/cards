package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicDestruction extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_destruction.csv", numLinesToSkip = 1)
    public void testDestruction(String template) {
        int card = create(template, player, Components.Zone.CREATURE_ZONE);
        destroy(card);
        assertHasComponent(card, Components.Zone.GRAVEYARD);
    }
}
