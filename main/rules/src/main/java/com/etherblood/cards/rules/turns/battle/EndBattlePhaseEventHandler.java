package com.etherblood.cards.rules.turns.battle;

import com.etherblood.cards.rules.turns.main.*;
import com.etherblood.cards.rules.battle.*;
import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import com.etherblood.cards.rules.turns.TurnPhase;
import com.etherblood.cards.rules.turns.respond.StartRespondPhaseEvent;
import com.etherblood.cards.rules.turns.upkeep.StartUpkeepPhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class EndBattlePhaseEventHandler implements EventHandler<EndBattlePhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int phaseKey;

    public EndBattlePhaseEventHandler(EntityData data, EventQueue events, Logger log, int phaseKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.phaseKey = phaseKey;
    }

    @Override
    public void onEvent(EndBattlePhaseEvent event) {
        assert data.get(event.player, phaseKey) == TurnPhase.BATTLE.ordinal();
        log.info("battle phase of {} ended", event.player);
        events.trigger(new StartUpkeepPhaseEvent(event.player));
    }

}
