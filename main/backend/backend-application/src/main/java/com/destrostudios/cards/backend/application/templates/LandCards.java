package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

/**
 *
 * @author Philipp
 */
public class LandCards extends CardsUtil {

    public static int plains(EntityData data) {
        return land(data, "Plains", Components.Color.WHITE);
    }

    public static int mountains(EntityData data) {
        return land(data, "Mountains", Components.Color.RED);
    }

    public static int forest(EntityData data) {
        return land(data, "Forest", Components.Color.GREEN);
    }

    public static int island(EntityData data) {
        return land(data, "Island", Components.Color.BLUE);
    }

    public static int swamp(EntityData data) {
        return land(data, "Swamp", Components.Color.BLACK);
    }

    public static int land(EntityData data, String displayName, ComponentDefinition<Void> colorComponent) {
        int landCard = data.createEntity();
        data.setComponent(landCard, colorComponent);
        data.setComponent(landCard, Components.LAND_CARD);
        data.setComponent(landCard, Components.DISPLAY_NAME, displayName);
        int playSpell = data.createEntity();
        handActivated(data, playSpell);
        data.setComponent(playSpell, Components.Spell.Effect.ADD_TO_BOARD);
        int tapSpell = data.createEntity();
        boardActivated(data, tapSpell);
        int tapSpellCost = data.createEntity();
        data.setComponent(tapSpellCost, Components.Cost.TAP);
        data.setComponent(tapSpell, Components.Spell.COST_ENTITY, tapSpellCost);
        data.setComponent(landCard, Components.SPELL_ENTITIES, new int[]{playSpell, tapSpell});
        return landCard;
    }
}
