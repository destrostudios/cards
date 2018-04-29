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
public class DeclareBlockEventHandler implements EventHandler<DeclareBlockEvent> {
    
    private static final Logger LOG = LoggerFactory.getLogger(DeclareBlockEventHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final int declareBlockKey = Components.DECLARED_BLOCK;

    public DeclareBlockEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(DeclareBlockEvent event) {
        LOG.info("{} will block {}", event.source, event.target);
        data.set(event.source, declareBlockKey, event.target);
    }

}
