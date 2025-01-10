package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleDiscoverer extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_discoverer.csv", numLinesToSkip = 1)
    public void testDiscoverOnSummon(String template, String discoverPoolName) {
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card, DISCOVER_OPTIONS);
        getAndAssertFirstCard(player, Components.Zone.HAND, getDiscoveredCardName(DiscoverPool.valueOf(discoverPoolName)));
    }
}
