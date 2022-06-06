package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class SpellDescriptionGenerator {

    public static String generateDescription(EntityData data, int spell) {
        String description = data.getComponent(spell, Components.DESCRIPTION);
        if (description != null) {
            Integer maximumCastsPerTurn = data.getComponent(spell, Components.Spell.MAXIMUM_CASTS_PER_TURN);
            if (maximumCastsPerTurn != null) {
                description += " (Can only be activated " + ((maximumCastsPerTurn == 1) ? "once" : maximumCastsPerTurn + " times") + " per turn)";
            }
        }
        return description;
    }
}
