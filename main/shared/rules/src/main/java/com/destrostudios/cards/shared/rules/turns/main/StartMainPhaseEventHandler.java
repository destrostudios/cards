package com.destrostudios.cards.shared.rules.turns.main;

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
public class StartMainPhaseEventHandler implements EventHandler<StartMainPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartMainPhaseEventHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public StartMainPhaseEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(StartMainPhaseEvent event) {
        LOG.info("main phase of {} started", event.player);
        data.set(event.player, Components.TURN_PHASE, TurnPhase.MAIN);
    }

}
