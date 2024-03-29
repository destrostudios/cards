package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicSummon extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_summon.csv", numLinesToSkip = 1)
    public void testSummon(String template) {
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(card, Components.Zone.CREATURE_ZONE);
    }
}
