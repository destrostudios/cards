package com.destrostudios.cards.sandbox;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntSet;
import com.destrostudios.cards.shared.rules.Components;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class EntityDebugMapper {

    private final ComponentDebugData[] debugComponents;

    public EntityDebugMapper() {
        debugComponents = new ComponentDebugData[Components.COMPONENTS_COUNT];
        for (int component = 0; component < debugComponents.length; component++) {
            debugComponents[component] = new ComponentDebugData(Integer.toString(component), x -> x);
        }
    }

    public void register(int component, String name) {
        register(component, name, x -> x);
    }

    public void register(int component, String name, IntFunction<?> converter) {
        debugComponents[component] = new ComponentDebugData(name, converter);
    }

    public Map<Integer, Map<String, Object>> toDebugObjects(EntityData data, IntSet entities) {
        Map<Integer, Map<String, Object>> result = new TreeMap<>();
        entities.foreach(entity -> {
            result.put(entity, toDebugObject(data, entity));
        });
        return result;
    }

    private Map<String, Object> toDebugObject(EntityData data, int entity) {
        Map<String, Object> result = new TreeMap<>();
        for (int component = 0; component < debugComponents.length; component++) {
            ComponentDebugData debug = debugComponents[component];
            if (data.has(entity, component)) {
                int value = data.get(entity, component);
                result.put(debug.name, debug.converter.apply(value));
            }
        }
        return result;
    }
}
