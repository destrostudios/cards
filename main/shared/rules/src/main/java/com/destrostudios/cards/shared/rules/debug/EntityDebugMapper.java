package com.destrostudios.cards.shared.rules.debug;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Philipp
 */
public class EntityDebugMapper {

    public Map<Integer, Map<String, Object>> toDebugObjects(EntityData data) {
        Map<Integer, Map<String, Object>> result = new TreeMap<>();
        data.getEntities().foreach(entity -> {
            result.put(entity, toDebugObject(data, entity));
        });
        return result;
    }

    private Map<String, Object> toDebugObject(EntityData data, int entity) {
        Map<String, Object> result = new TreeMap<>();
        for (ComponentDefinition<?> component : data.knownComponents()) {
            if (data.has(entity, component)) {
                Object value = data.getComponent(entity, component);
                result.put(component.getName(), value);
            }
        }
        return result;
    }
}
