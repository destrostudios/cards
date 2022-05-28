package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class SetAvailableManaHandler extends GameEventHandler<SetAvailableManaEvent> {

    @Override
    public void handle(SetAvailableManaEvent event, NetworkRandom random) {
        data.setComponent(event.player, Components.AVAILABLE_MANA, event.manaAmount);
    }
}
