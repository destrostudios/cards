package com.etherblood.cards.rules.turns.upkeep;

import com.etherblood.cards.rules.turns.battle.*;
import com.etherblood.cards.rules.turns.main.*;
import com.etherblood.cards.rules.battle.*;
import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import com.etherblood.cards.rules.turns.TurnPhase;
import com.etherblood.cards.rules.turns.respond.StartRespondPhaseEvent;
import com.etherblood.cards.rules.turns.upkeep.StartUpkeepPhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class EndUpkeepPhaseEventHandler implements EventHandler<EndUpkeepPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey;

    public EndUpkeepPhaseEventHandler(EntityData data, EventQueue events, Logger log, int phaseKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.phaseKey = phaseKey;
    }

    @Override
    public void onEvent(EndUpkeepPhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.UPKEEP.ordinal();
        log.info("upkeep phase of {} ended", event.player);
        events.trigger(new StartMainPhaseEvent(event.player));
    }

}
