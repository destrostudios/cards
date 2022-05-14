package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class SetManaHandler extends GameEventHandler<SetManaEvent> {

    @Override
    public void handle(SetManaEvent event) {
        data.setComponent(event.player, Components.MANA, event.manaAmount);
    }
}
