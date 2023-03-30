package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class ZoneUtil {

    public static void addCardToZone(EntityData data, int card, ComponentDefinition<Integer> zone) {
        int owner = data.getComponent(card, Components.OWNED_BY);
        data.setComponent(card, zone, data.query(zone).count(c -> data.getComponent(c, Components.OWNED_BY) == owner));
    }
}
