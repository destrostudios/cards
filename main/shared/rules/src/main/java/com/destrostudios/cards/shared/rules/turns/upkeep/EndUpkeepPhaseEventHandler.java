package com.destrostudios.cards.shared.rules.turns.upkeep;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndUpkeepPhaseEventHandler implements EventHandler<EndUpkeepPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndUpkeepPhaseEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;
    private final int phaseKey = Components.TURN_PHASE;

    public EndUpkeepPhaseEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(EndUpkeepPhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.UPKEEP.ordinal();
        LOG.info("upkeep phase of {} ended", event.player);
        events.trigger(new StartMainPhaseEvent(event.player));
    }

}
