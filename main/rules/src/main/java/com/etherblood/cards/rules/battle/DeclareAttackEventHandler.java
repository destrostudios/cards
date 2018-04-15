package com.etherblood.cards.rules.battle;

import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class DeclareAttackEventHandler implements EventHandler<DeclareAttackEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int declareAttackKey;

    public DeclareAttackEventHandler(EntityData data, EventQueue events, Logger log, int declareAttackKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.declareAttackKey = declareAttackKey;
    }

    @Override
    public void onEvent(DeclareAttackEvent event) {
        log.info("{} will attack {}", event.source, event.target);
        data.set(event.source, declareAttackKey, event.target);
    }

}
