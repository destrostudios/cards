package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddBuffHandler extends GameEventHandler<AddBuffEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddBuffHandler.class);

    @Override
    public void handle(AddBuffEvent event, NetworkRandom random) {
        LOG.info("Add buff {} to {}", event.buff, event.target);
        ArrayUtil.add(data, event.target, Components.BUFFS, event.buff);
    }
}
