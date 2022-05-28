package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class PayManaHandler extends GameEventHandler<PayManaEvent> {

    @Override
    public void handle(PayManaEvent event, NetworkRandom random) {
        if (event.manaAmount != 0) {
            int currentMana = data.getOptionalComponent(event.player, Components.MANA).orElse(0);
            data.setComponent(event.player, Components.MANA, currentMana - event.manaAmount);
        }
    }
}
