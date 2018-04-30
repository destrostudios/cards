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
public class DeclareAttackEventHandler implements EventHandler<DeclareAttackEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeclareAttackEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;

    public DeclareAttackEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(DeclareAttackEvent event) {
        LOG.info("{} will attack {}", event.source, event.target);
        data.set(event.source, Components.DECLARED_ATTACK, event.target);
    }

}
