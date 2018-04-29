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
public class DamageEventHandler implements EventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageEventHandler.class);
    private final EntityData data;
    private final EventQueue events;
    private final int healthKey = Components.HEALTH;

    public DamageEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(DamageEvent event) {
        LOG.info("dealing {} damage to {}", event.damage, event.target);
        events.response(new SetHealthEvent(event.target, data.getOrElse(event.target, healthKey, 0) - event.damage));
    }

}
