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
        return land(data, "Plains", Components.Color.WHITE, Components.ManaAmount.WHITE, 1);
    }

    public static int mountain(EntityData data) {
        return land(data, "Mountain", Components.Color.RED, Components.ManaAmount.RED, 1);
    }

    public static int forest(EntityData data) {
        return land(data, "Forest", Components.Color.GREEN, Components.ManaAmount.GREEN, 1);
    }

    public static int island(EntityData data) {
        return land(data, "Island", Components.Color.BLUE, Components.ManaAmount.BLUE, 1);
    }

    public static int swamp(EntityData data) {
        return land(data, "Swamp", Components.Color.BLACK, Components.ManaAmount.BLACK, 1);
    }

    public static int land(EntityData data, String displayName, ComponentDefinition<Void> colorComponent, ComponentDefinition<Integer> manaAmountComponent, int manaAmount) {
        int landCard = data.createEntity();
        data.setComponent(landCard, colorComponent);
        data.setComponent(landCard, Components.LAND_CARD);
        data.setComponent(landCard, Components.DISPLAY_NAME, displayName);
        int playSpell = data.createEntity();
        handActivated(data, playSpell);
        data.setComponent(playSpell, Components.Spell.Effect.Zones.ADD_TO_BOARD);
        int tapSpell = data.createEntity();
        boardActivated(data, tapSpell);
        int tapSpellCost = data.createEntity();
        data.setComponent(tapSpellCost, Components.Cost.TAP);
        data.setComponent(tapSpell, Components.Spell.COST_ENTITY, tapSpellCost);
        int tapSpellEffectGainMana = data.createEntity();
        data.setComponent(tapSpellEffectGainMana, manaAmountComponent, manaAmount);
        data.setComponent(tapSpell, Components.Spell.Effect.GAIN_MANA, tapSpellEffectGainMana);
        data.setComponent(landCard, Components.SPELL_ENTITIES, new int[]{playSpell, tapSpell});
        return landCard;
    }
}
