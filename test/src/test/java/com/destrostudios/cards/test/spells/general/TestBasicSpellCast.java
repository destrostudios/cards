package com.destrostudios.cards.test.spells.general;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestBasicSpellCast extends TestGame {

    @ParameterizedTest
    @CsvFileSource(resources = "/spells/basic_spell_cast.csv", numLinesToSkip = 1)
    public void testCast(String template) {
        int card = create(template, player, Components.HAND);
        castFromHand(card);
        assertHasComponent(card, Components.GRAVEYARD);
    }
}
