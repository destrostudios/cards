package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class HealthUtil {

    public static Integer getEffectiveHealth(EntityData data, int entity) {
        Integer health = data.getComponent(entity, Components.HEALTH);
        if (health != null) {
            int damaged = data.getOptionalComponent(entity, Components.DAMAGED).orElse(0);
            return (health - damaged);
        }
        return null;
    }
}
