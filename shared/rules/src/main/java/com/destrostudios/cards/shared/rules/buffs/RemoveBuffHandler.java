package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveBuffHandler extends GameEventHandler<RemoveBuffEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveBuffHandler.class);

    @Override
    public void handle(RemoveBuffEvent event, NetworkRandom random) {
        LOG.debug("Removing buff {} from {}", inspect(event.buff), inspect(event.target));
        ArrayUtil.remove(data, event.target, Components.BUFFS, event.buff);
        // TODO: Remove buff entity if it was an evaluated copy? Overkill for now?
    }
}
