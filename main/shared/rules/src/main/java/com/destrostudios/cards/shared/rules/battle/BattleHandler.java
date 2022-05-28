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
public class BattleHandler extends GameEventHandler<BattleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattleHandler.class);

    @Override
    public void handle(BattleEvent event, NetworkRandom random) {
        LOG.info("{} is battling {}", event.source, event.target);
        events.fire(new DamageEvent(event.target, data.getOptionalComponent(event.source, Components.ATTACK).orElse(0)), random);
        events.fire(new DamageEvent(event.source, data.getOptionalComponent(event.target, Components.ATTACK).orElse(0)), random);
    }
}
