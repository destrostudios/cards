package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayManaHandler extends GameEventHandler<PayManaEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PayManaHandler.class);

    @Override
    public void handle(PayManaEvent event, NetworkRandom random) {
        if (event.manaAmount != 0) {
            int currentMana = data.getOptionalComponent(event.player, Components.MANA).orElse(0);
            int newMana = currentMana - event.manaAmount;
            LOG.debug("Player {} is paying {} mana (current mana = {}, new mana = {})", inspect(event.player), event.manaAmount, currentMana, newMana);
            data.setComponent(event.player, Components.MANA, newMana);
        }
    }
}
