package com.etherblood.cards.sandbox.rules;

import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetHealthEventHandler implements EventHandler<SetHealthEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int healthKey;

    public SetHealthEventHandler(EntityData data, EventQueue events, Logger log, int healthKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.healthKey = healthKey;
    }

    @Override
    public void onEvent(SetHealthEvent event) {
        log.info("setting health of {} to {}", event.target, event.health);
        data.set(event.target, healthKey, event.health);
    }

}
