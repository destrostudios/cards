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
public class AttackHandler extends GameEventHandler<AttackEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AttackHandler.class);

    @Override
    public void handle(AttackEvent event, NetworkRandom random) {
        LOG.info("{} is attacking {}", event.source, event.target);
        events.fire(new BattleEvent(event.source, event.target), random);
        data.setComponent(event.source, Components.HAS_ATTACKED);
    }
}
