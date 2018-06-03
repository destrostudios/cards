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

    private final SimpleEntityData data;

    public EntityMapper(EntityData data) {
        this.data = getAsSimple(data);
    }

    private static SimpleEntityData getAsSimple(EntityData data) {
        if (data instanceof SimpleEntityData) {
            return (SimpleEntityData) data;
        }
        throw new UnsupportedOperationException();
    }

    public int getNextEntity() {
        return data.getNextEntity();
    }

    public void setNextEntity(int value) {
        data.setNextEntity(value);
    }

    public Map<ComponentDefinition<?>, Map<Integer, Object>> toComponentTables() {
        Map<ComponentDefinition<?>, Map<Integer, Object>> result = new LinkedHashMap<>();
        data.knownComponents().forEach(component -> {
            TreeMap<Integer, Object> map = new TreeMap<>();
            result.put(component, map);
            data.query(component).list().forEach(entity -> {
                map.put(entity, data.getComponent(entity, component));
            });
        });
        return result;
    }

    public Map<Integer, Map<ComponentDefinition<?>, Object>> toObjectMaps(IntPredicate... predicates) {
        Map<Integer, Map<ComponentDefinition<?>, Object>> result = new TreeMap<>();
        data.getEntities().forEach(entity -> {
            for (IntPredicate predicate : predicates) {
                if (!predicate.test(entity)) {
                    return;
                }
            }
            result.put(entity, toMap(entity));
        });
        return result;
    }

    private Map<ComponentDefinition<?>, Object> toMap(int entity) {
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
