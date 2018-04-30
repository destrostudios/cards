package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.battle.BattleEventHandler;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class UpkeepDrawEventHandler implements EventHandler<StartUpkeepPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattleEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;

    public UpkeepDrawEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(StartUpkeepPhaseEvent event) {
        LOG.info("drawing upkeep card");
        events.response(new DrawCardEvent(event.player));
    }

}
