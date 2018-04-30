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
public class BattleEventHandler implements EventHandler<BattleEvent> {
    
    private static final Logger LOG = LoggerFactory.getLogger(BattleEventHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public BattleEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(BattleEvent event) {
        LOG.info("{} is attacking {}", event.source, event.target);
        events.response(new DamageEvent(event.target, data.getOrElse(event.source, Components.ATTACK, 0)));
        events.response(new DamageEvent(event.source, data.getOrElse(event.target, Components.ATTACK, 0)));
    }

}
