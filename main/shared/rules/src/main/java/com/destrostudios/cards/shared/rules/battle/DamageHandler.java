package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DamageHandler implements GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, DamageEvent event) {
        LOG.info("dealing {} damage to {}", event.damage, event.target);
        events.fireSubEvent(new SetHealthEvent(event.target, data.getComponentOrDefault(event.target, Components.HEALTH, 0) - event.damage));
    }

}
