package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.BuffUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddBuffHandler extends GameEventHandler<AddBuffEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddBuffHandler.class);

    @Override
    public void handle(AddBuffEvent event, NetworkRandom random) {
        LOG.debug("Adding buff " + inspect(event.buff) + " to " + inspect(event.target));
        BuffUtil.add(data, event.target, event.buff);
    }
}
