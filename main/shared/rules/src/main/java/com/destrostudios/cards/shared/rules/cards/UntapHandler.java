package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class UntapHandler extends GameEventHandler<UntapEvent> {

    @Override
    public void handle(UntapEvent event) {
        data.removeComponent(event.card, Components.TAPPED);
    }
}
