package com.destrostudios.cards.shared.rules.turns.upkeep;

import com.destrostudios.cards.shared.rules.turns.main.*;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
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
