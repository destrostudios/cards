package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.model.CardIdentifier;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.cards.Foil;

public class ModelUtil {

    public static int createCard(EntityData data, CardIdentifier cardIdentifier) {
        int card = data.createEntity();
        EntityTemplate.loadTemplate(data, card, cardIdentifier.getCard().getPath());
        // TODO: Cleanup?
        if (GameConstants.FOIL_NAME_ARTWORK.equals(cardIdentifier.getFoil().getName())) {
            data.setComponent(card, Components.FOIL, Foil.ARTWORK);
        } else if (GameConstants.FOIL_NAME_FULL.equals(cardIdentifier.getFoil().getName())) {
            data.setComponent(card, Components.FOIL, Foil.FULL);
        }
        return card;
    }
}
