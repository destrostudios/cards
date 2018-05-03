package com.destrostudios.cards.shared.entities;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class DebugEntityMapper {

    public Map<Integer, Map<String, Object>> toDebugObjects(EntityData data, IntPredicate... predicates) {
        Map<Integer, Map<String, Object>> result = new TreeMap<>();
        data.getEntities().foreach(entity -> {
            for (IntPredicate predicate : predicates) {
                if(!predicate.test(entity)) {
                    return;
                }
            }
            result.put(entity, toDebugObject(data, entity));
        });
        return result;
    }

    private Map<String, Object> toDebugObject(EntityData data, int entity) {
        Map<String, Object> result = new TreeMap<>();
        for (ComponentDefinition<?> component : data.knownComponents()) {
            if (data.hasComponent(entity, component)) {
                Object value = data.getComponent(entity, component);
                result.put(component.getName(), value);
            }
        }
        return result;
    }
}
