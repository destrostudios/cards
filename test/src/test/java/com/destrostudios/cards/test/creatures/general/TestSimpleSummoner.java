package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleSummoner extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_summoner.csv", numLinesToSkip = 1)
    public void testSummonOnSummon(String template, String summonName) {
        int card = create(template, player, Components.HAND);
        castFromHand(card);
        assertOneCard(player, Components.CREATURE_ZONE, summonName);
    }
}
