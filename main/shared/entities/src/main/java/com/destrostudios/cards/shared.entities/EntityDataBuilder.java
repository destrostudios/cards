package com.destrostudios.cards.shared.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Philipp
 */
public class EntityDataBuilder {

    private final List<ComponentDefinition<?>> components = new ArrayList<>();
    private int nextComponentKey = 0;

    public ComponentDefinition<Integer> withComponent(String name) {
        return withComponent(name, x -> x);
    }

    public <T> ComponentDefinition<T> withComponent(String name, ValueMapping<T> mapping) {
        ComponentDefinition<T> component = new ComponentDefinition<>(name, nextComponentKey++, mapping);
        components.add(component);
        return component;
    }

    public EntityData build() {
        return new EntityData(nextComponentKey);
    }

    public Map<Integer, Map<String, Object>> toDebugMap(EntityData data, EntityPool pool) {
        Map<Integer, Map<String, Object>> map = new TreeMap<>();
        pool.getEntities().foreach(entity -> {
            map.put(entity, new TreeMap<>());
        });
        for (ComponentDefinition<?> component : components) {
            data.foreachEntityWithComponent(component.getKey(), (entity, value) -> {
                map.get(entity).put(component.getName(), component.getMapping().fromInt(value));
            });
        }
        return map;
    }

}
