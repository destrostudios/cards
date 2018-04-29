package com.destrostudios.cards.shared.rules.turns.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class EndBattlePhaseEventHandler implements EventHandler<EndBattlePhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey = Components.TURN_PHASE;

    public EndBattlePhaseEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(EndBattlePhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.BATTLE.ordinal();
        log.info("battle phase of {} ended", event.player);
        events.trigger(new StartUpkeepPhaseEvent(event.player));
    }

}
