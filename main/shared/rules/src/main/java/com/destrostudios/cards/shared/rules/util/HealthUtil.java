package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class HealthUtil {

    public static int getEffectiveHealth(EntityData data, int entity) {
        int health = data.getOptionalComponent(entity, Components.HEALTH).orElse(0);
        int damaged = data.getOptionalComponent(entity, Components.DAMAGED).orElse(0);
        return (health - damaged);
    }
}
