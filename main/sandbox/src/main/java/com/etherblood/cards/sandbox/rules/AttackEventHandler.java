package com.etherblood.cards.sandbox.rules;

import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class AttackEventHandler implements EventHandler<AttackEvent>{

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int attackKey;

    public AttackEventHandler(EntityData data, EventQueue events, Logger log, int attackKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.attackKey = attackKey;
    }
    
    @Override
    public void onEvent(AttackEvent event) {
        log.info("{} is attacking {}", event.source, event.target);
        events.trigger(new DamageEvent(event.target, data.getOrElse(event.source, attackKey, 0)));
    }

}
