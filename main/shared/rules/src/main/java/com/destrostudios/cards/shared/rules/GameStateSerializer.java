package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.EntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameStateSerializer {

    public FullGameState exportState(EntityData data) {
        EntityMapper mapper = new EntityMapper(data);
        Map<ComponentDefinition<?>, Map<Integer, Object>> tables = mapper.toComponentTables();

        List<Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>>> list = new ArrayList<>();
        for (Map.Entry<ComponentDefinition<?>, Map<Integer, Object>> entry : tables.entrySet()) {
            list.add(new Tuple<>(entry.getKey(), entry.getValue().entrySet().stream().map(e -> new Tuple<>(e.getKey(), e.getValue())).collect(Collectors.toList())));
        }

        return new FullGameState(list, mapper.getNextEntity());
    }
}
