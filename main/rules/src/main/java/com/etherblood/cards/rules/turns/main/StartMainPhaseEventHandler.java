package com.etherblood.cards.rules.turns.main;

import com.etherblood.cards.rules.battle.*;
import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import com.etherblood.cards.rules.turns.TurnPhase;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseEventHandler implements EventHandler<StartMainPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey;

    public StartMainPhaseEventHandler(EntityData data, EventQueue events, Logger log, int phaseKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.phaseKey = phaseKey;
    }

    @Override
    public void onEvent(StartMainPhaseEvent event) {
        log.info("main phase of {} started", event.player);
        data.set(event.player, phaseKey, TurnPhase.MAIN.ordinal());
    }

}
