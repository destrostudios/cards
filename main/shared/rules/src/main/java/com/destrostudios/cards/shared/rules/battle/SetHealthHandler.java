package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetHealthHandler implements GameEventHandler<SetHealthEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetHealthHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, SetHealthEvent event) {
        LOG.info("setting health of {} to {}", event.target, event.health);
        data.setComponent(event.target, Components.HEALTH, event.health);
    }

}
