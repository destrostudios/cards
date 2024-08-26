package com.destrostudios.cards.test.spells.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleDraw extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/spells/simple_draw.csv", numLinesToSkip = 1)
    public void testDrawOnCast(String template, int draw) {
        int[] libraryCards = createCards(draw, player, Components.Zone.LIBRARY);
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card, opponent);
        assertHasComponent(libraryCards, Components.Zone.HAND);
    }
}
