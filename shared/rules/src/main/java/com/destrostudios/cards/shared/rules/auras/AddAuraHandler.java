package com.destrostudios.cards.shared.rules.auras;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddAuraHandler extends GameEventHandler<AddAuraEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddAuraHandler.class);

    @Override
    public void handle(AddAuraEvent event, NetworkRandom random) {
        LOG.info("Add aura {} to {}", event.aura, event.target);
        ArrayUtil.add(data, event.target, Components.AURAS, event.aura);
    }
}
