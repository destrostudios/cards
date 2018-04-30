package com.destrostudios.cards.shared.rules.turns.upkeep;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.BattleEventHandler;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartUpkeepPhaseEventHandler implements EventHandler<StartUpkeepPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattleEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;

    public StartUpkeepPhaseEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(StartUpkeepPhaseEvent event) {
        LOG.info("upkeep phase of {} started", event.player);
        data.set(event.player, Components.TURN_PHASE, TurnPhase.UPKEEP);
        events.trigger(new EndUpkeepPhaseEvent(event.player));
    }

}
