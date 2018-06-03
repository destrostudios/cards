package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromHandHandler extends GameEventHandler<RemoveCardFromHandEvent> {

    @Override
    public void handle(RemoveCardFromHandEvent event) {
        events.fireSubEvent(new RemoveCardFromZoneEvent(event.card, Components.HAND_CARDS));
    }
}
