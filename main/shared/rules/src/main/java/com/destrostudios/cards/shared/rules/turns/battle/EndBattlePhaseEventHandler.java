package com.destrostudios.cards.shared.rules.turns.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndBattlePhaseEventHandler implements EventHandler<EndBattlePhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndBattlePhaseEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;

    public EndBattlePhaseEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(EndBattlePhaseEvent event) {
        LOG.info("battle phase of {} ended", event.player);
        events.trigger(new StartUpkeepPhaseEvent(event.player));
    }

}
