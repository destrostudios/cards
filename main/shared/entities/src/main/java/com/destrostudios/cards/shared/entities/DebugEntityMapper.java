package com.destrostudios.cards.shared.entities;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class DebugEntityMapper {

    public Map<Integer, Map<ComponentDefinition<?>, Object>> toDebugObjects(EntityData data, IntPredicate... predicates) {
        if(data instanceof SimpleEntityData) {
            return toDebugObjects((SimpleEntityData)data, predicates);
        }
        if(data instanceof HasEntityData) {
            return toDebugObjects(((HasEntityData)data).getEntityData(), predicates);
        }
        throw new UnsupportedOperationException();
    }
    
    private Map<Integer, Map<ComponentDefinition<?>, Object>> toDebugObjects(SimpleEntityData data, IntPredicate... predicates) {
        Map<Integer, Map<ComponentDefinition<?>, Object>> result = new TreeMap<>();
        data.getEntities().forEach(entity -> {
            for (IntPredicate predicate : predicates) {
                if(!predicate.test(entity)) {
                    return;
                }
            }
            result.put(entity, toDebugObject(data, entity));
        });
        return result;
    }

    private Map<ComponentDefinition<?>, Object> toDebugObject(SimpleEntityData data, int entity) {
        Map<ComponentDefinition<?>, Object> result = new TreeMap<>();
        for (ComponentDefinition<?> component : data.knownComponents()) {
            if (data.hasComponent(entity, component)) {
                Object value = data.getComponent(entity, component);
                result.put(component, value);
            }
        }
        return result;
    }
}
