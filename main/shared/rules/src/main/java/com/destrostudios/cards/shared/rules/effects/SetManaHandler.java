package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class SetManaHandler extends GameEventHandler<SetManaEvent> {

    @Override
    public void handle(SetManaEvent event, NetworkRandom random) {
        data.setComponent(event.player, Components.MANA, event.manaAmount);
    }
}
