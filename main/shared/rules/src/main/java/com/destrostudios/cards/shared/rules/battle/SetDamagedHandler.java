package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetDamagedHandler extends GameEventHandler<SetDamagedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetDamagedHandler.class);

    @Override
    public void handle(SetDamagedEvent event) {
        LOG.info("setting damaged of {} to {}", event.target, event.damaged);
        data.setComponent(event.target, Components.DAMAGED, event.damaged);
    }
}
