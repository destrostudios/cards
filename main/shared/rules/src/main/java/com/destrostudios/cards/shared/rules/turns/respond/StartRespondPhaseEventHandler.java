package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartRespondPhaseEventHandler implements EventHandler<StartRespondPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartRespondPhaseEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;
    private final int phaseKey = Components.TURN_PHASE;

    public StartRespondPhaseEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(StartRespondPhaseEvent event) {
        LOG.info("respond phase of {} started", event.player);
        data.set(event.player, phaseKey, TurnPhase.RESPOND.ordinal());
    }

}
