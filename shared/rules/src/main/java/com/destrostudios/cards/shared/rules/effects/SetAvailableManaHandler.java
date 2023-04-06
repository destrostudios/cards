package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetAvailableManaHandler extends GameEventHandler<SetAvailableManaEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetAvailableManaHandler.class);

    @Override
    public void handle(SetAvailableManaEvent event, NetworkRandom random) {
        LOG.info("Set available mana of player " + inspect(event.player) + " to " + event.manaAmount);
        data.setComponent(event.player, Components.AVAILABLE_MANA, event.manaAmount);
    }
}
