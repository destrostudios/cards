package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExpressionEntity {

    private EntityData data;
    private int entity;

    public int getAttack() {
        return data.getComponent(entity, Components.Stats.ATTACK);
    }

    public int getHealth() {
        return data.getComponent(entity, Components.Stats.HEALTH);
    }

    public boolean isDamaged() {
        return data.hasComponent(entity, Components.Stats.DAMAGED);
    }
}
