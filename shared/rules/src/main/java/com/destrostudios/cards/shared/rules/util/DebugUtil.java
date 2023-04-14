package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import lombok.AllArgsConstructor;

import java.util.Arrays;

public class DebugUtil {

    @AllArgsConstructor
    public static class EntityDebugText_Iterable {

        private EntityData data;
        private Iterable<Integer> entities;

        @Override
        public String toString() {
            return getDebugText(data, entities);
        }
    }

    @AllArgsConstructor
    public static class EntityDebugText_Array {

        private EntityData data;
        private int[] entities;

        @Override
        public String toString() {
            return getDebugText(data, Arrays.stream(entities).boxed().toList());
        }
    }

    private static String getDebugText(EntityData data, Iterable<Integer> entities) {
        String text = "";
        int length = 0;
        for (int entity : entities) {
            if (length > 0) {
                text += ", ";
            }
            text += getDebugText(data, entity);
            length++;
        }
        if ((length == 0) || (length > 1)) {
            text = "[" + text + "]";
        }
        return text;
    }

    private static String getDebugText(EntityData data, int entity) {
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
