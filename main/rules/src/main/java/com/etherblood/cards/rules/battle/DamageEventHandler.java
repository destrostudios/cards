package com.etherblood.cards.rules.battle;

import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class DamageEventHandler implements EventHandler<DamageEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int healthKey;

    public DamageEventHandler(EntityData data, EventQueue events, Logger log, int healthKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.healthKey = healthKey;
    }

    @Override
    public void onEvent(DamageEvent event) {
        log.info("dealing {} damage to {}", event.damage, event.target);
        events.response(new SetHealthEvent(event.target, data.getOrElse(event.target, healthKey, 0) - event.damage));
    }

}
