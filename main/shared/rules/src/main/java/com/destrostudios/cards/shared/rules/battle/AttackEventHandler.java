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
public class AttackEventHandler implements EventHandler<AttackEvent> {
    
    private static final Logger LOG = LoggerFactory.getLogger(AttackEventHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final int attackKey = Components.ATTACK;

    public AttackEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(AttackEvent event) {
        LOG.info("{} is attacking {}", event.source, event.target);
        events.response(new DamageEvent(event.target, data.getOrElse(event.source, attackKey, 0)));
        events.response(new DamageEvent(event.source, data.getOrElse(event.target, attackKey, 0)));
    }

}
