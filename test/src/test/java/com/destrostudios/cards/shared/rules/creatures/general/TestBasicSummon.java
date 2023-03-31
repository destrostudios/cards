package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicSummon extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/basic_summon.csv")
    public void testSummon(String template) {
        int card = create(template, player, Components.HAND);
        castFromHand(card);
        assertHasComponent(card, Components.BOARD);
    }
}
