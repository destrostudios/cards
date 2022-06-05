package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DamageHandler extends GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    @Override
    public void handle(DamageEvent event, NetworkRandom random) {
        LOG.info("dealing {} damage to {}", event.damage, event.target);
        if (data.getOptionalComponent(event.target, Components.Ability.DIVINE_SHIELD).orElse(false)) {
            LOG.info("remove divine shield from {}", event.target);
            data.setComponent(event.target, Components.Ability.DIVINE_SHIELD, false);
        } else {
            events.fire(new SetDamagedEvent(event.target, data.getOptionalComponent(event.target, Components.DAMAGED).orElse(0) + event.damage), random);
        }
    }
}
