package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class StartRespondPhaseEventHandler implements EventHandler<StartRespondPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey = Components.TURN_PHASE;

    public StartRespondPhaseEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(StartRespondPhaseEvent event) {
        log.info("respond phase of {} started", event.player);
        data.set(event.player, phaseKey, TurnPhase.RESPOND.ordinal());
    }

}
