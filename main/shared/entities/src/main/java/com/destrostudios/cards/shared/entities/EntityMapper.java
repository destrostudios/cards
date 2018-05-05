package com.destrostudios.cards.shared.entities;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class EntityMapper {

    public int getNextEntity(EntityData data) {
        if (data instanceof SimpleEntityData) {
            return ((SimpleEntityData) data).getNextEntity();
        }
        if (data instanceof HasEntityData) {
            return getNextEntity(((HasEntityData) data).getEntityData());
        }
        throw new UnsupportedOperationException();
    }

    public void setNextEntity(EntityData data, int value) {
        if (data instanceof SimpleEntityData) {
            ((SimpleEntityData) data).setNextEntity(value);
        } else if (data instanceof HasEntityData) {
            setNextEntity(((HasEntityData) data).getEntityData(), value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Map<ComponentDefinition<?>, Map<Integer, Object>> toTables(EntityData data) {
        if (data instanceof SimpleEntityData) {
            return toTables((SimpleEntityData) data);
        }
        if (data instanceof HasEntityData) {
            return toTables(((HasEntityData) data).getEntityData());
        }
        throw new UnsupportedOperationException();
    }

    public Map<ComponentDefinition<?>, Map<Integer, Object>> toTables(SimpleEntityData data) {
        Map<ComponentDefinition<?>, Map<Integer, Object>> result = new LinkedHashMap<>();
        data.knownComponents().forEach(component -> {
            TreeMap<Integer, Object> map = new TreeMap<>();
            result.put(component, map);
            data.entities(component).foreach(entity -> {
                map.put(entity, data.getComponent(entity, component));
            });
        });
        return result;
    }

    public Map<Integer, Map<ComponentDefinition<?>, Object>> toMaps(EntityData data, IntPredicate... predicates) {
        if (data instanceof SimpleEntityData) {
            return toMaps((SimpleEntityData) data, predicates);
        }
        if (data instanceof HasEntityData) {
            return toMaps(((HasEntityData) data).getEntityData(), predicates);
        }
        throw new UnsupportedOperationException();
    }

    private Map<Integer, Map<ComponentDefinition<?>, Object>> toMaps(SimpleEntityData data, IntPredicate... predicates) {
        Map<Integer, Map<ComponentDefinition<?>, Object>> result = new TreeMap<>();
        data.getEntities().forEach(entity -> {
            for (IntPredicate predicate : predicates) {
                if (!predicate.test(entity)) {
                    return;
                }
            }
            result.put(entity, toMap(data, entity));
        });
        return result;
    }

    private Map<ComponentDefinition<?>, Object> toMap(SimpleEntityData data, int entity) {
        Map<ComponentDefinition<?>, Object> result = new TreeMap<>(Comparator.comparing(ComponentDefinition::getName));
        for (ComponentDefinition<?> component : data.knownComponents()) {
            if (data.hasComponent(entity, component)) {
                Object value = data.getComponent(entity, component);
                result.put(component, value);
            }
        }
        return result;
    }
}
