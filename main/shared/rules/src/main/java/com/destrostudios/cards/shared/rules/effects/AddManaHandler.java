package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddManaHandler extends GameEventHandler<AddManaEvent> {

    @Override
    public void handle(AddManaEvent event) {
        int currentMana = data.getOptionalComponent(event.player, Components.MANA).orElse(0);
        events.fire(new SetManaEvent(event.player, currentMana + event.manaAmount));
    }
}
