package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.AllCards;

public class TestLibraries {

    public static int[] getLibrary(EntityData data) {
        int[] cards = new int[AllCards.TEMPLATES.length];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = EntityTemplate.createFromTemplate(data, AllCards.TEMPLATES[i]);
        }
        return cards;
    }
}
