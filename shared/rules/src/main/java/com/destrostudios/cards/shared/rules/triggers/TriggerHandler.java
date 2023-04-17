package com.destrostudios.cards.shared.rules.triggers;

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
public class TriggerHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerHandler.class);

    private ComponentsTriggers.TriggerDefinition<T> trigger;

    @Override
    public void handle(GameContext context, T event) {
        EntityData data = context.getData();
        LOG.debug("Checking triggers of type {} for {}", trigger, event);
        for (int entity : data.list(trigger.component())) {
            TriggerUtil.trigger(data.getComponent(entity, trigger.component()), entity, trigger.getTargets().apply(event), context.getEvents());
        }
    }
}
