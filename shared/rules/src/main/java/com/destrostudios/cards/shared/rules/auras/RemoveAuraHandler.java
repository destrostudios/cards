package com.destrostudios.cards.shared.rules.auras;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveAuraHandler extends GameEventHandler<RemoveAuraEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveAuraHandler.class);

    @Override
    public void handle(RemoveAuraEvent event, NetworkRandom random) {
        LOG.info("remove aura {} from {}", event.aura, event.target);
        ArrayUtil.remove(data, event.target, Components.AURAS, event.aura);
    }
}
