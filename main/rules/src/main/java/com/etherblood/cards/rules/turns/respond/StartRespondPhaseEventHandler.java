package com.etherblood.cards.rules.turns.respond;

import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import com.etherblood.cards.rules.turns.TurnPhase;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class StartRespondPhaseEventHandler implements EventHandler<StartRespondPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey;

    public StartRespondPhaseEventHandler(EntityData data, EventQueue events, Logger log, int phaseKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.phaseKey = phaseKey;
    }

    @Override
    public void onEvent(StartRespondPhaseEvent event) {
        log.info("respond phase of {} started", event.player);
        data.set(event.player, phaseKey, TurnPhase.RESPOND.ordinal());
    }

}
