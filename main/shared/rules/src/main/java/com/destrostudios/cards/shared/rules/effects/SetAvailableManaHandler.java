package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class SetAvailableManaHandler extends GameEventHandler<SetAvailableManaEvent> {

    @Override
    public void handle(SetAvailableManaEvent event) {
        data.setComponent(event.player, Components.AVAILABLE_MANA, event.manaAmount);
    }
}
