package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetHealthHandler implements EventHandler<SetHealthEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetHealthHandler.class);
    
    private final EntityData data;
    private final EventQueue events;

    public SetHealthHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(SetHealthEvent event) {
        LOG.info("setting health of {} to {}", event.target, event.health);
        data.set(event.target, Components.HEALTH, event.health);
    }

}
