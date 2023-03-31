package com.destrostudios.cards.shared.rules.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleDrawer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_drawer.csv", numLinesToSkip = 1)
    public void testDrawOnSummon(String template, int draw) {
        createCards(player, draw + 1, Components.LIBRARY);
        int card = create(template, player, Components.HAND);
        castFromHand(card);
        assertCardsCount(player, Components.LIBRARY, 1);
        assertCardsCount(player, Components.HAND, draw);
    }
}
