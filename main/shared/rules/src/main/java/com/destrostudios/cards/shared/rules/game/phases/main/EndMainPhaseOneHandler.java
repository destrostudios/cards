package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.phases.attack.StartAttackPhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseOneHandler extends GameEventHandler<EndMainPhaseOneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndMainPhaseOneHandler.class);

    @Override
    public void handle(EndMainPhaseOneEvent event) {
        LOG.debug("main phase 1 of {} ended.", event.player);
        events.fire(new StartAttackPhaseEvent(event.player));
    }
}
