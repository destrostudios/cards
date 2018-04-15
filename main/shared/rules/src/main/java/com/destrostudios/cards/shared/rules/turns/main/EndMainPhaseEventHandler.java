package com.destrostudios.cards.shared.rules.turns.main;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.respond.StartRespondPhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseEventHandler implements EventHandler<EndMainPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey, nextPlayerKey;

    public EndMainPhaseEventHandler(EntityData data, EventQueue events, Logger log, int phaseKey, int nextPlayerKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.phaseKey = phaseKey;
        this.nextPlayerKey = nextPlayerKey;
    }

    @Override
    public void onEvent(EndMainPhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.MAIN.ordinal();
        log.info("main phase of {} ended", event.player);
        data.remove(event.player, phaseKey);
        int nextPlayer = data.get(event.player, nextPlayerKey);
        events.trigger(new StartRespondPhaseEvent(nextPlayer));
    }

}
