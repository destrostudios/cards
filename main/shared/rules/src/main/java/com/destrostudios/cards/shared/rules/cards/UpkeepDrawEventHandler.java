package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.ComponentValue;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class UpkeepDrawEventHandler implements EventHandler<StartUpkeepPhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;

    public UpkeepDrawEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(StartUpkeepPhaseEvent event) {
        log.info("drawing upkeep card");
        events.response(new DrawCardEvent(event.player));
    }

}
