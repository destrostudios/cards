package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDamagedHandler extends GameEventHandler<SetDamagedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetDamagedHandler.class);

    @Override
    public void handle(SetDamagedEvent event, NetworkRandom random) {
        LOG.info("setting damaged of {} to {}", event.target, event.damaged);
        if (event.damaged > 0) {
            data.setComponent(event.target, Components.DAMAGED, event.damaged);
        } else {
            data.removeComponent(event.target, Components.DAMAGED);
        }
    }
}
