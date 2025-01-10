package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.ComponentsTriggers;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class CheckTriggeredTriggersHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CheckTriggeredTriggersHandler.class);

    private ComponentsTriggers.TriggeredTriggerDefinition<T> triggeredTriggerDefinition;

    @Override
    public void handle(GameContext context, T event) {
        EntityData data = context.getData();
        LOG.debug("Checking triggered triggers of type {} for {}", triggeredTriggerDefinition.component().getName(), event);
        for (int entity : data.list(triggeredTriggerDefinition.component())) {
            Components.TriggeredTrigger[] triggeredTriggers = data.getComponent(entity, triggeredTriggerDefinition.component());
            for (Components.TriggeredTrigger triggeredTrigger : triggeredTriggers) {
                TriggerUtil.triggerIfPossible(data, triggeredTrigger.getTrigger(), entity, new int[] { triggeredTrigger.getTarget() }, null, context.getEvents());
            }
            data.removeComponent(entity, triggeredTriggerDefinition.component());
        }
    }
}
