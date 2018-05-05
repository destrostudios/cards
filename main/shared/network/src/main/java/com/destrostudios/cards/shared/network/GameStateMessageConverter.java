package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.EntityMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Philipp
 */
public class GameStateMessageConverter {

    public FullGameStateMessage toMessage(EntityData data) {
        Map<ComponentDefinition<?>, Map<Integer, Object>> tables = new EntityMapper().toTables(data);

        List<Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>>> list = new ArrayList<>();
        for (Map.Entry<ComponentDefinition<?>, Map<Integer, Object>> entry : tables.entrySet()) {
            list.add(new Tuple<>(entry.getKey(), entry.getValue().entrySet().stream().map(e -> new Tuple<>(e.getKey(), e.getValue())).collect(Collectors.toList())));
        }

        return new FullGameStateMessage(list, new EntityMapper().getNextEntity(data));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void fromMessage(FullGameStateMessage message, EntityData data) {
        new EntityMapper().setNextEntity(data, message.getNextEntity());
        for (Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>> tuple : message.getList()) {
            ComponentDefinition component = tuple.getKey();
            for (Tuple<Integer, Object> tuple1 : tuple.getValue()) {
                int entity = tuple1.getKey();
                Object value = tuple1.getValue();
                data.setComponent(entity, component, value);
            }
        }
    }
}
