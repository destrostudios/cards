package com.destrostudios.cards.backend.application.libraries;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.AllCards;

public class TestLibraries {

    public static int[] custom(EntityData data) {
        int[] cards = new int[AllCards.TEMPLATES.length];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = EntityTemplate.createFromTemplate(data, AllCards.TEMPLATES[i]);
        }
        return cards;
    }

    public static int[] random(EntityData data, int size) {
        int[] cards = new int[size];
        for (int i = 0; i < size; i++) {
            cards[i] = RandomCards.randomCard(data);
        }
        return cards;
    }
}
