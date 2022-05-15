package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

/**
 *
 * @author Philipp
 */
public class CardsUtil {

    protected static int creature(EntityData data, String name, int attack, int health) {
        int entity = data.createEntity();
        data.setComponent(entity, Components.NAME, name);
        data.setComponent(entity, Components.CREATURE_CARD);
        data.setComponent(entity, Components.ATTACK, attack);
        data.setComponent(entity, Components.HEALTH, health);
        
        return entity;
    }

    protected static int summon(EntityData data, int cost) {
        int spell = data.createEntity();
        data.setComponent(spell, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(spell, Components.Spell.COST_ENTITY, cost);
        int sourceEffect = data.createEntity();
        data.setComponent(sourceEffect, Components.Spell.Effect.Zones.ADD_TO_BOARD);
        data.setComponent(spell, Components.Spell.SOURCE_EFFECT, sourceEffect);
        return spell;
    }
}
