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
public class DeclareBlockEventHandler implements EventHandler<DeclareBlockEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int declareBlockKey = Components.DECLARED_BLOCK;

    public DeclareBlockEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(DeclareBlockEvent event) {
        log.info("{} will block {}", event.source, event.target);
        data.set(event.source, declareBlockKey, event.target);
    }

}
