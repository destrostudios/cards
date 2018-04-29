package com.destrostudios.cards.shared.rules.turns.upkeep;

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
public class StartUpkeepPhaseEventHandler implements EventHandler<StartUpkeepPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey = Components.TURN_PHASE;

    public StartUpkeepPhaseEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(StartUpkeepPhaseEvent event) {
        log.info("upkeep phase of {} started", event.player);
        data.set(event.player, phaseKey, TurnPhase.UPKEEP.ordinal());
        events.trigger(new EndUpkeepPhaseEvent(event.player));
    }

}
