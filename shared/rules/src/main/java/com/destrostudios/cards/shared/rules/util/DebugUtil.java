package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.Arrays;

public class DebugUtil {

    public static String getDebugText(EntityData data, int[] entities) {
        return getDebugText(data, Arrays.stream(entities).boxed().toList());
    }

    public static String getDebugText(EntityData data, Iterable<Integer> entities) {
        String text = "";
        for (int entity : entities) {
            if (!text.isEmpty()) {
                text += ", ";
            }
            text += getDebugText(data, entity);
        }
        return text;
    }

    public static String getDebugText(EntityData data, int entity) {
        String text = "";
        String name = data.getComponent(entity, Components.NAME);
        if (name != null) {
            text += "\"" + name + "\"";
        }
        String entityIndexText = "#" + entity;
        text = (text.isEmpty() ? entityIndexText : text + " (" + entityIndexText + ")");
        return text;
    }
}
