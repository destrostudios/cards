package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleDrawer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_drawer.csv", numLinesToSkip = 1)
    public void testDrawOnSummon(String template, int draw) {
        int[] libraryCards = createCards(draw, player, Components.Zone.LIBRARY);
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(libraryCards, Components.Zone.HAND);
    }
}
