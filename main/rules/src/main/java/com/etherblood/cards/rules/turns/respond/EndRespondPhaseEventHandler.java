package com.etherblood.cards.rules.turns.respond;

import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import com.etherblood.cards.rules.turns.TurnPhase;
import com.etherblood.cards.rules.turns.battle.StartBattlePhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class EndRespondPhaseEventHandler implements EventHandler<EndRespondPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey;

    public EndRespondPhaseEventHandler(EntityData data, EventQueue events, Logger log, int phaseKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.phaseKey = phaseKey;
    }

    @Override
    public void onEvent(EndRespondPhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.RESPOND.ordinal();
        log.info("respond phase of {} ended", event.player);
        events.trigger(new StartBattlePhaseEvent(event.player));
    }

}
