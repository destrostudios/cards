package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.model.CardIdentifier;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.cards.Foil;

public class CardUtil {

    public static int createCard(EntityData data, CardIdentifier cardIdentifier) {
        return createCard(data, cardIdentifier.getCard().getPath(), mapFoil(cardIdentifier));
    }

    // TODO: Cleanup?
    private static Foil mapFoil(CardIdentifier cardIdentifier) {
        if (GameConstants.FOIL_NAME_ARTWORK.equals(cardIdentifier.getFoil().getName())) {
            return Foil.ARTWORK;
        } else if (GameConstants.FOIL_NAME_FULL.equals(cardIdentifier.getFoil().getName())) {
            return Foil.FULL;
        }
        return null;
    }

    public static int createCard(EntityData data, String path, Foil foil) {
        int card = createCard(data, path);
        if (foil != null) {
            data.setComponent(card, Components.FOIL, foil);
        }
        return card;
    }

    public static int createCard(EntityData data, String path) {
        int card = data.createEntity();
        EntityTemplate.loadTemplate(data, card, path);
        return card;
    }
}
