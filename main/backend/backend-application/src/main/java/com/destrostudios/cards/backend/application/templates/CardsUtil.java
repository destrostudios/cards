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
        data.setComponent(entity, Components.DISPLAY_NAME, name);
        data.setComponent(entity, Components.CREATURE_CARD);
        data.setComponent(entity, Components.ATTACK, attack);
        data.setComponent(entity, Components.HEALTH, health);
        
        return entity;
    }
    
    protected static int summon(EntityData data, int cost) {
        int entity = data.createEntity();
        handActivated(data, entity);
        data.setComponent(entity, Components.Spell.COST_ENTITY, cost);
        data.setComponent(entity, Components.Spell.Effect.ADD_TO_BOARD);
        return entity;
    }
    
    protected static int handActivated(EntityData data, int entity) {
        data.setComponent(entity, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(entity, Components.Spell.CastCondition.ATTACK_PHASE);
        data.setComponent(entity, Components.Spell.CastCondition.MAIN_PHASE);
        return entity;
    }
    
    protected static int boardActivated(EntityData data, int entity) {
        data.setComponent(entity, Components.Spell.CastCondition.FROM_BOARD);
        data.setComponent(entity, Components.Spell.CastCondition.ATTACK_PHASE);
        data.setComponent(entity, Components.Spell.CastCondition.MAIN_PHASE);
        return entity;
    }
}
