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
public class AttackEventHandler implements EventHandler<AttackEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int attackKey = Components.ATTACK;

    public AttackEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(AttackEvent event) {
        log.info("{} is attacking {}", event.source, event.target);
        events.response(new DamageEvent(event.target, data.getOrElse(event.source, attackKey, 0)));
        events.response(new DamageEvent(event.source, data.getOrElse(event.target, attackKey, 0)));
    }

}
