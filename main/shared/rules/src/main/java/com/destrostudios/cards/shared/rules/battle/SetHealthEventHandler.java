package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetHealthEventHandler implements EventHandler<SetHealthEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int healthKey = Components.HEALTH;

    public SetHealthEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(SetHealthEvent event) {
        log.info("setting health of {} to {}", event.target, event.health);
        data.set(event.target, healthKey, event.health);
    }

}
