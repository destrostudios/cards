package com.destrostudios.cards.shared.rules.triggers;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.ComponentsTriggers;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class TriggerHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerHandler.class);

    private ComponentsTriggers.TriggerDefinition<T> trigger;

    @Override
    public void handle(T event, NetworkRandom random) {
        LOG.debug("Checking triggers of type {} for {}", trigger, event);
        for (int entity : data.query(trigger.component()).list()) {
            TriggerUtil.trigger(data.getComponent(entity, trigger.component()), entity, trigger.getTargets().apply(event), events, random);
        }
    }
}
