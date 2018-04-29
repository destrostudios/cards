package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndRespondPhaseEventHandler implements EventHandler<EndRespondPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndRespondPhaseEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;
    private final int phaseKey = Components.TURN_PHASE;

    public EndRespondPhaseEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(EndRespondPhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.RESPOND.ordinal();
        LOG.info("respond phase of {} ended", event.player);
        events.trigger(new StartBattlePhaseEvent(event.player));
    }

}
