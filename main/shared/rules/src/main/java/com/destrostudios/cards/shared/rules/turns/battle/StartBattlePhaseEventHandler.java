package com.destrostudios.cards.shared.rules.turns.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartBattlePhaseEventHandler implements EventHandler<StartBattlePhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartBattlePhaseEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;

    public StartBattlePhaseEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(StartBattlePhaseEvent event) {
        LOG.info("battle phase of {} started", event.player);
        data.set(event.player, Components.TURN_PHASE, TurnPhase.BATTLE);
        events.trigger(new EndBattlePhaseEvent(event.player));
    }

}
