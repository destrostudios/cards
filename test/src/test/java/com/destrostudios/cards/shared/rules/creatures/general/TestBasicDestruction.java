package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicDestruction extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_destruction.csv")
    public void testDestruction(String template) {
        int card = create(template, player, Components.CREATURE_ZONE);
        destroy(card);
        assertHasComponent(card, Components.GRAVEYARD);
    }
}
