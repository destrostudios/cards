package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetHealthHandler extends GameEventHandler<SetHealthEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetHealthHandler.class);

    @Override
    public void handle(SetHealthEvent event) {
        LOG.info("setting health of {} to {}", event.target, event.health);
        data.setComponent(event.target, Components.HEALTH, event.health);
    }

}
