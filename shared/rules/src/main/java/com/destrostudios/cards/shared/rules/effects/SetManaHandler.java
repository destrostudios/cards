package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetManaHandler extends GameEventHandler<SetManaEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetManaHandler.class);

    @Override
    public void handle(SetManaEvent event, NetworkRandom random) {
        LOG.info("Setting mana of player " + inspect(event.player) + " to " + event.manaAmount);
        data.setComponent(event.player, Components.MANA, event.manaAmount);
    }
}
