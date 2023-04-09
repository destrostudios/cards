package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import lombok.AllArgsConstructor;

import java.util.Arrays;

public class DebugUtil {

    @AllArgsConstructor
    public static class EntityDebugText {

        public EntityDebugText(EntityData data, int[] entities) {
            this(data, Arrays.stream(entities).boxed().toList());
        }
        private EntityData data;
        private Iterable<Integer> entities;

        @Override
        public String toString() {
            return getDebugText(data, entities);
        }
    }

    private static String getDebugText(EntityData data, Iterable<Integer> entities) {
        String text = "";
        for (int entity : entities) {
            if (!text.isEmpty()) {
                text += ", ";
            }
            text += getDebugText(data, entity);
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
