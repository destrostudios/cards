package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class EndRespondPhaseEventHandler implements EventHandler<EndRespondPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey = Components.TURN_PHASE;

    public EndRespondPhaseEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(EndRespondPhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.RESPOND.ordinal();
        log.info("respond phase of {} ended", event.player);
        events.trigger(new StartBattlePhaseEvent(event.player));
    }

}
