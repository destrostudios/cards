package com.destrostudios.cards.test.creatures.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestSimpleTutor extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/creatures/simple_tutor.csv", numLinesToSkip = 1)
    public void testDrawTargetOnSummon(String template, String targetTemplate) {
        int target = create(targetTemplate, player, Components.Zone.LIBRARY);
        int card = create(template, player, Components.Zone.HAND);
        castFromHand(card);
        assertHasComponent(target, Components.Zone.HAND);
    }
}
