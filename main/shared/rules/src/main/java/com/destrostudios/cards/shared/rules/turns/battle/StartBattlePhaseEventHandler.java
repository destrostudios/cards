package com.destrostudios.cards.shared.rules.turns.battle;

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
public class StartBattlePhaseEventHandler implements EventHandler<StartBattlePhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey = Components.TURN_PHASE;

    public StartBattlePhaseEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(StartBattlePhaseEvent event) {
        log.info("battle phase of {} started", event.player);
        data.set(event.player, phaseKey, TurnPhase.BATTLE.ordinal());
        events.trigger(new EndBattlePhaseEvent(event.player));
    }

}
