package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DamageHandler extends GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    @Override
    public void handle(DamageEvent event) {
        LOG.info("dealing {} damage to {}", event.damage, event.target);
        events.fireSubEvent(new SetHealthEvent(event.target, data.getOptionalComponent(event.target, Components.HEALTH).orElse(0) - event.damage));
    }

}
