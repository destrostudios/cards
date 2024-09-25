package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.ComponentsTriggers;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class CheckTriggersHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CheckTriggersHandler.class);

    private ComponentsTriggers.TriggerDefinition<T> triggerDefinition;

    @Override
    public void handle(GameContext context, T event) {
        EntityData data = context.getData();
        LOG.debug("Checking triggers of type {} for {}", triggerDefinition.component().getName(), event);
        for (int entity : data.list(triggerDefinition.component())) {
            TriggerUtil.triggerIfPossible(data, data.getComponent(entity, triggerDefinition.component()), entity, triggerDefinition.getTargets().apply(event), context.getEvents());
        }
    }
}
