package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class TapHandler extends GameEventHandler<TapEvent> {

    @Override
    public void handle(TapEvent event) {
        data.setComponent(event.card, Components.TAPPED);
    }
}
